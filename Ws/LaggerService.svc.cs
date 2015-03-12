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
    }
}
