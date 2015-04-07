using LaggerServer.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace LaggerServer
{
    public class LaggerService : ILaggerService
    {
        #region Testy

        public User GetUser(string id)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var user = ctx.Users.Where(x => x.ID_User == Convert.ToInt32(id)).FirstOrDefault();

                return user;
            }
        }

        public string TestConnection()
        {
            return "Connected success.";
        }

        public string TestConnectionValue(string value)
        {
            return "You sended value: " + value;
        }

        public TestowaKlasa TestJsonGet()
        {
            return new TestowaKlasa()
            {
                Login = "Test",
                Password = "Tajne"
            };
        }

        public TestowaKlasa TestJsonPost(TestowaKlasa value)
        {
            value.Login += " Nowy";
            value.Password += " Nowy";
            return value;
        }

        #endregion

        public LoginUserResponse LoginUser(LoginUserRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var response = new LoginUserResponse();

                var user = ctx.Users.Where(x => x.Login.Equals(request.Login) && !x.Blocked).FirstOrDefault();

                if (user != null)
                {
                    if (user.Password.Equals(request.Password))
                    {
                        response.IdUser = user.ID_User;
                        response.Status = LoginUserStatus.Success;
                    }
                    else
                    {
                        response.Status = LoginUserStatus.IncorrectPassword;
                    }
                }
                else
                {
                    response.Status = LoginUserStatus.UnregisteredUser;
                }

                return response;
            }
        }

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

        public GetFriendsResponse GetFriends(GetFriendsRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var list = from uf in ctx.UserFriends
                           join u in ctx.Users
                           on uf.IDUser equals u.ID_User
                           where uf.Status == (short)UserFriendStatus.Accepted
                           && uf.IDFriend == request.IdUser
                           && !u.Blocked
                           && !uf.Blocked
                           select new Friend(u);

                var list2 = from uf in ctx.UserFriends
                            join u in ctx.Users
                            on uf.IDFriend equals u.ID_User
                            where uf.Status == (short)UserFriendStatus.Accepted
                            && uf.IDUser == request.IdUser
                            && !u.Blocked
                            && !uf.Blocked
                            select new Friend(u);

                return new GetFriendsResponse()
                {
                    List = list.Union(list2).ToList()
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


        public List<Friend> GetFriendsTest(GetFriendsRequest request)
        {
            throw new NotImplementedException();
        }

        public AddFriendResponse AddFriend(AddFriendRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var entity = (from uf in ctx.UserFriends
                              where uf.Status == (short)UserFriendStatus.NotAccepted
                              && (uf.IDUser == request.IdUser && uf.IDFriend == request.IdFriend
                              || uf.IDFriend == request.IdUser && uf.IDUser == request.IdFriend)
                              && !uf.Blocked
                              select uf).FirstOrDefault();

                if (entity != null)
                {
                    entity.Status = (short)UserFriendStatus.Accepted;
                }
                else
                {
                    entity = new UserFriend()
                    {
                        IDUser = request.IdUser,
                        IDFriend = request.IdFriend,
                        Status = (short)UserFriendStatus.NotAccepted
                    };

                    ctx.UserFriends.InsertOnSubmit(entity);
                }

                ctx.SubmitChanges();

                return new AddFriendResponse();
            }
        }

        public RemoveFriendResponse RemoveFriend(RemoveFriendRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var entity = (from uf in ctx.UserFriends
                              where uf.Status == (short)UserFriendStatus.NotAccepted
                              && (uf.IDUser == request.IdUser && uf.IDFriend == request.IdFriend
                              || uf.IDFriend == request.IdUser && uf.IDUser == request.IdFriend)
                              && !uf.Blocked
                              select uf).FirstOrDefault();

                if (entity != null)
                {
                    entity.Status = (short)UserFriendStatus.Refused;
                }
                else
                {
                    entity = new UserFriend()
                    {
                        IDUser = request.IdUser,
                        IDFriend = request.IdFriend,
                        Status = (short)UserFriendStatus.Refused
                    };

                    ctx.UserFriends.InsertOnSubmit(entity);
                }

                ctx.SubmitChanges();

                return new RemoveFriendResponse();
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

                // edycja znajomych

                return new EditMeetingResponse();
            }
        }
    }
}
