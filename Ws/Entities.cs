using LaggerServer.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace LaggerServer
{
    [DataContract]
    public class Meeting
    {
        [DataMember]
        public int Id { get; set; }

        [DataMember]
        public String Name { get; set; }

        [DataMember]
        public String LocationName { get; set; }

        [DataMember]
        public Decimal Latitude { get; set; }

        [DataMember]
        public Decimal Longitude { get; set; }

        [DataMember]
        public DateTime StartTime { get; set; }

        [DataMember]
        public DateTime EndTime { get; set; }

        public Meeting(Event entity)
        {
            Id = entity.ID_Event;
            Name = entity.Name;
            LocationName = entity.LocationName;
            Latitude = entity.Latitude;
            Longitude = entity.Longitude;
            StartTime = entity.StartTime;
            EndTime = entity.EndTime;
        }
    }

    [DataContract]
    public class Friend
    {
        [DataMember]
        public int Id { get; set; }

        [DataMember]
        public String Login { get; set; }

        [DataMember]
        public String Email { get; set; }

        [DataMember]
        public String Phone { get; set; }

        public Friend(User entity)
        {
            Id = entity.ID_User;
            Login = entity.Login;
            Email = entity.Email;
            Phone = entity.Phone;
        }
    }
}