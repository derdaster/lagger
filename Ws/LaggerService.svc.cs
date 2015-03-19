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

                var user = ctx.Users.Where(x => x.Login.Equals(request.Login)).FirstOrDefault();

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
                           select new Friend(u);

                var list2 = from uf in ctx.UserFriends
                            join u in ctx.Users
                            on uf.IDFriend equals u.ID_User
                            where uf.Status == (short)UserFriendStatus.Accepted
                            && uf.IDUser == request.IdUser
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
                            select new Meeting(e)).ToList();

                return new GetMeetingInvitationsResponse()
                {
                    List = list
                };
            }
        }
    }
}
