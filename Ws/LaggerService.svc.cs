using LaggerServer.Database;
using LaggerServer.Meetings;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;
using System.Web.UI;

namespace LaggerServer
{
    public partial class LaggerService : ILaggerService
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

        public string ResetInvitation(string id)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var invitations = (from ue in ctx.UserEvents
                                   join e in ctx.Events
                                   on ue.IDEvent equals e.ID_Event
                                   where ue.IDUser == Convert.ToInt32(id)
                                   && !ue.Blocked
                                   && !e.Blocked
                                   select ue).ToList();

                foreach (var inv in invitations)
                {
                    inv.Status = (short)UserEventStatus.NotAccepted;
                }

                if (invitations.Count > 0)
                {
                    invitations.First().Status = (short)UserEventStatus.Accepted;
                }

                ctx.SubmitChanges();

                return "Zaktualizowano: " + invitations.Count;
            }
        }

        public string ShowMeetings(string id)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var invitations = (from ue in ctx.UserEvents
                                   join e in ctx.Events
                                   on ue.IDEvent equals e.ID_Event
                                   where ue.IDUser == Convert.ToInt32(id)
                                   && !ue.Blocked
                                   && !e.Blocked
                                   select new Pair(ue, e)).ToList();

                var result = "Stan na dzień: " + DateTime.Now;

                result += " [Not accepted]:";

                foreach (var inv in invitations.Where(x => ((UserEvent)x.First).Status == (short)UserEventStatus.NotAccepted))
                {
                    result += Test((Event)inv.Second) + ", ";
                }

                result += " [Accepted]:";

                foreach (var inv in invitations.Where(x => ((UserEvent)x.First).Status == (short)UserEventStatus.Accepted))
                {
                    result += Test((Event)inv.Second) + ", ";
                }

                result += " [Refused]:";

                foreach (var inv in invitations.Where(x => ((UserEvent)x.First).Status == (short)UserEventStatus.Refused))
                {
                    result += Test((Event)inv.Second) + ", ";
                }

                result += Environment.NewLine;

                return result;
            }
        }

        private string Test(Event e)
        {
            return String.Format("({0}) {1}", e.ID_Event, e.Name);
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

    }
}
