using LaggerServer.Database;
using LaggerServer.Meetings;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer
{
    public partial class LaggerService : ILaggerService
    {
        public GetMeetingsResponse GetMeetings(GetMeetingsRequest request)
        {
            try
            {
                LogDiagnostic("GetMeetings", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var list = (from e in ctx.Events
                                join ue in ctx.UserEvents
                                on e.ID_Event equals ue.IDEvent
                                join u in ctx.Users
                                on e.IDOrganizer equals u.ID_User
                                where ue.IDUser == request.IdUser
                                && ue.Status == (short)UserEventStatus.Accepted
                                && !e.Blocked
                                && !ue.Blocked
                                && !u.Blocked
                                select new Meeting(e, u)).ToList();

                    return new GetMeetingsResponse()
                    {
                        Meetings = list
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("GetMettings Error", ex);
                return null;
            }
        }

        public GetMeetingInvitationsResponse GetMeetingInvitations(GetMeetingInvitationsRequest request)
        {
            try
            {
                LogDiagnostic("GetMeetingInvitations", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var list = (from e in ctx.Events
                                join ue in ctx.UserEvents
                                on e.ID_Event equals ue.IDEvent
                                join u in ctx.Users
                                on e.IDOrganizer equals u.ID_User
                                where ue.IDUser == request.IdUser
                                && ue.Status == (short)UserEventStatus.NotAccepted
                                && !e.Blocked
                                && !ue.Blocked
                                && !u.Blocked
                                select new Meeting(e, u)).ToList();

                    return new GetMeetingInvitationsResponse()
                    {
                        MeetingInvitations = list
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("GetMeetingInvitations Error", ex);
                return null;
            }
        }

        public MeetingInvitationAcceptResponse MeetingInvitationAccept(MeetingInvitationAcceptRequest request)
        {
            try
            {
                LogDiagnostic("MeetingInvitationAccept", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var invitation = (from ue in ctx.UserEvents
                                      where ue.IDUser == request.IdUser
                                      && ue.IDEvent == request.IdMeeting
                                      && ue.Status == (short)UserEventStatus.NotAccepted
                                      && !ue.Blocked
                                      select ue).FirstOrDefault();

                    if (invitation != null)
                    {
                        invitation.Status = (short)(request.Accept ? UserEventStatus.Accepted : UserEventStatus.Refused);
                        ctx.SubmitChanges();
                    }

                    return new MeetingInvitationAcceptResponse()
                    {
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("MeetingInvitationAccept Error", ex);
                return null;
            }
        }

        public AddMeetingResponse AddMeeting(AddMeetingRequest request)
        {
            try
            {
                LogDiagnostic("AddMeeting", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var entity = new Event()
                    {
                        Name = request.Name,
                        IDOrganizer = request.IdUser,
                        LocationName = request.LocationName,
                        Latitude = request.Latitude,
                        Longitude = request.Longitude,
                        StartTime = request.StartTime,
                        EndTime = request.EndTime,
                        LastEditDate = DateTime.UtcNow,
                        CreationDate = DateTime.UtcNow,
                        Blocked = false
                    };

                    ctx.Events.InsertOnSubmit(entity);
                    ctx.SubmitChanges();

                    foreach (var user in request.UsersList)
                    {
                        ctx.UserEvents.InsertOnSubmit(new UserEvent()
                        {
                            IDEvent = entity.ID_Event,
                            IDUser = user,
                            Status = (short)UserEventStatus.NotAccepted,
                            LastEditDate = DateTime.UtcNow,
                            CreationDate = DateTime.UtcNow,
                            Blocked = false
                        });
                    }

                    ctx.SubmitChanges();

                    return new AddMeetingResponse() { IdMeeting = entity.ID_Event };
                }
            }
            catch (Exception ex)
            {
                SetError("AddMeeting Error", ex);
                return null;
            }
        }

        public EditMeetingResponse EditMeeting(EditMeetingRequest request)
        {
            try
            {
                LogDiagnostic("EditMeeting", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var entity = ctx.Events.Where(x => x.ID_Event == request.IdMeeting).FirstOrDefault();

                    if (entity != null)
                    {
                        entity.Name = request.Name;
                        entity.LocationName = request.LocationName;
                        entity.Latitude = request.Latitude;
                        entity.Longitude = request.Longitude;
                        entity.StartTime = request.StartTime;
                        entity.EndTime = request.EndTime;
                    }

                    var listUserMeeting = ctx.UserEvents.Where(x => x.IDEvent == request.IdMeeting && !x.Blocked);

                    var listToRemove = listUserMeeting.Where(x => !request.UsersList.Contains(x.IDUser));

                    foreach (var userEvent in listToRemove)
                    {
                        userEvent.Blocked = true;
                    }

                    var listToAdd = request.UsersList.Where(x => !listUserMeeting.Select(y => y.IDUser).Contains(x));

                    foreach (var user in listToAdd)
                    {
                        ctx.UserEvents.InsertOnSubmit(new UserEvent()
                        {
                            IDEvent = entity.ID_Event,
                            IDUser = user,
                            Status = (short)UserEventStatus.NotAccepted,
                            LastEditDate = DateTime.UtcNow,
                            CreationDate = DateTime.UtcNow,
                            Blocked = false
                        });
                    }

                    ctx.SubmitChanges();

                    return new EditMeetingResponse();
                }
            }
            catch (Exception ex)
            {
                SetError("EditMeeting Error", ex);
                return null;
            }
        }


        public RemoveMeetingResponse RemoveMeeting(RemoveMeetingRequest request)
        {
            try
            {
                LogDiagnostic("RemoveMeeting", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var entity = (from e in ctx.Events
                                  where e.ID_Event == request.IdMeeting
                                  select e).FirstOrDefault();

                    if (entity == null)
                    {
                        throw new Exception("Nie ma takiego wydarzenia.");
                    }

                    if (entity.IDOrganizer != request.IdUser)
                    {
                        throw new Exception("Użytkownik nie jest organizatorem tego wydarzenia.");
                    }

                    entity.Blocked = true;

                    ctx.SubmitChanges();

                    return new RemoveMeetingResponse();
                }
            }
            catch (Exception ex)
            {
                SetError("RemoveMeeting Error", ex);
                return null;
            }
        }
    }
}