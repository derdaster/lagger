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
        [DataMember(Name = "id", Order = 0)]
        public int Id { get; set; }

        [DataMember(Name = "organizer", Order = 1)]
        public Friend Organizer { get; set; }

        [DataMember(Name = "name", Order = 2)]
        public String Name { get; set; }

        [DataMember(Name = "locationName", Order = 3)]
        public String LocationName { get; set; }

        [DataMember(Name = "latitude", Order = 4)]
        public Decimal Latitude { get; set; }

        [DataMember(Name = "longitude", Order = 5)]
        public Decimal Longitude { get; set; }

        [DataMember(Name = "startTime", Order = 6)]
        public DateTime StartTime { get; set; }

        [DataMember(Name = "endTime", Order = 7)]
        public DateTime EndTime { get; set; }

        public Meeting(Event entity, User user)
        {
            Id = entity.ID_Event;
            Name = entity.Name;
            LocationName = entity.LocationName;
            Latitude = entity.Latitude;
            Longitude = entity.Longitude;
            StartTime = entity.StartTime;
            EndTime = entity.EndTime;
            Organizer = new Friend(user);
        }
    }

    [DataContract]
    public class Friend
    {
        [DataMember(Name = "id")]
        public int Id { get; set; }

        [DataMember(Name = "login")]
        public String Login { get; set; }

        [DataMember(Name = "email")]
        public String Email { get; set; }

        [DataMember(Name = "phone")]
        public String Phone { get; set; }

        public Friend(User entity)
        {
            Id = entity.ID_User;
            Login = entity.Login;
            Email = entity.Email;
            Phone = entity.Phone;
        }
    }

    [DataContract]
    public class Position
    {
        [DataMember(Name = "idUser")]
        public int IdUser { get; set; }

        [DataMember(Name = "latitude")]
        public decimal Latitude { get; set; }

        [DataMember(Name = "longitude")]
        public decimal Longitude { get; set; }

        [DataMember(Name = "dateTime")]
        public DateTime DateTime { get; set; }

        [DataMember(Name = "arrivalTime")]
        public int ArrivalTime { get; set; }

        public Position(EventDetail entity)
        {
            IdUser = entity.IDUser;
            Latitude = entity.Latitude;
            Longitude = entity.Longitude;
        }
    }
}