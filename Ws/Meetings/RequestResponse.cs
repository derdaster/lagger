using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace LaggerServer.Meetings
{
    [DataContract]
    public class GetMeetingsRequest : Request
    {
    }

    [DataContract]
    public class GetMeetingsResponse : Response
    {
        [DataMember(Name = "list", Order = 0)]
        public List<Meeting> List { get; set; }
    }

    [DataContract]
    public class GetMeetingInvitationsRequest : Request
    {
    }

    [DataContract]
    public class GetMeetingInvitationsResponse : Response
    {
        [DataMember(Name = "list", Order = 0)]
        public List<Meeting> List { get; set; }
    }

    public enum UserEventStatus
    {
        NotAccepted = 0,
        Accepted = 1,
        Refused = 2
    }

    [DataContract]
    public class AddMeetingRequest : Request
    {
        [DataMember(Name = "name", Order = 0)]
        public String Name { get; set; }

        [DataMember(Name = "locationName", Order = 1)]
        public String LocationName { get; set; }

        [DataMember(Name = "latitude", Order = 2)]
        public decimal Latitude { get; set; }

        [DataMember(Name = "longitude", Order = 3)]
        public decimal Longitude { get; set; }

        [DataMember(Name = "startTime", Order = 4)]
        public DateTime StartTime { get; set; }

        [DataMember(Name = "endTime", Order = 5)]
        public DateTime EndTime { get; set; }

        [DataMember(Name = "usersList", Order = 6)]
        public List<int> UsersList { get; set; }
    }

    [DataContract]
    public class AddMeetingResponse : Response
    {
        [DataMember(Name = "idMeeting", Order = 0)]
        public int IdMeeting { get; set; }
    }

    [DataContract]
    public class EditMeetingRequest : Request
    {
        [DataMember(Name = "idMeeting", Order = 0)]
        public int IdMeeting { get; set; }

        [DataMember(Name = "name", Order = 1)]
        public String Name { get; set; }

        [DataMember(Name = "locationName", Order = 2)]
        public String LocationName { get; set; }

        [DataMember(Name = "latitude", Order = 3)]
        public decimal Latitude { get; set; }

        [DataMember(Name = "longitude", Order = 4)]
        public decimal Longitude { get; set; }

        [DataMember(Name = "startTime", Order = 5)]
        public DateTime StartTime { get; set; }

        [DataMember(Name = "endTime", Order = 6)]
        public DateTime EndTime { get; set; }

        [DataMember(Name = "usersList", Order = 7)]
        public List<int> UsersList { get; set; }
    }

    [DataContract]
    public class EditMeetingResponse : Response
    {
        [DataMember(Name = "list", Order = 0)]
        public List<Meeting> List { get; set; }
    }
}