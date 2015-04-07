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
            using (var ctx = new LaggerDbEntities())
            {
                var list = (from e in ctx.Events
                            join ue in ctx.UserEvents
                            on e.ID_Event equals ue.IDEvent
                            where ue.IDUser == request.IdUser
                            && ue.Status == (short)UserEventStatus.Accepted
                            && !e.Blocked
                            && !ue.Blocked
                            select new Meeting(e)).ToList();

                return new GetMeetingsResponse()
                {
                    List = list
                };
            }
        }

        public GetMeetingInvitationsResponse GetMeetingInvitations(GetMeetingInvitationsRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var list = (from e in ctx.Events
                            join ue in ctx.UserEvents
                            on e.ID_Event equals ue.IDEvent
                            where ue.IDUser == request.IdUser
                            && ue.Status == (short)UserEventStatus.NotAccepted
                            && !e.Blocked
                            && !ue.Blocked
                            select new Meeting(e)).ToList();

                return new GetMeetingInvitationsResponse()
                {
                    List = list
                };
            }
        }

        public AddMeetingResponse AddMeeting(AddMeetingRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                // IDOrganizatora
                var entity = new Event()
                {
                    Name = request.Name,
                    LocationName = request.LocationName,
                    Latitude = request.Latitude,
                    Longitude = request.Longitude,
                    StartTime = request.StartTime,
                    EndTime = request.EndTime
                };

                ctx.Events.InsertOnSubmit(entity);
                ctx.SubmitChanges();

                foreach (var user in request.UsersList)
                {
                    ctx.UserEvents.InsertOnSubmit(new UserEvent()
                    {
                        IDEvent = entity.ID_Event,
                        IDUser = user,
                        Status = (short)UserEventStatus.NotAccepted
                    });
                }

                ctx.SubmitChanges();

                return new AddMeetingResponse() { IdMeeting = entity.ID_Event };
            }
        }

        public EditMeetingResponse EditMeeting(EditMeetingRequest request)
        {
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
                        Status = (short)UserEventStatus.NotAccepted
                    });
                }

                ctx.SubmitChanges();

                return new EditMeetingResponse();
            }
        }
    }
}