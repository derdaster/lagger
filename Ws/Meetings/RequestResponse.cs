using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer.Meetings
{
    public class GetMeetingsRequest : Request
    {
    }

    public class GetMeetingsResponse
    {
        public List<Meeting> List { get; set; }
    }

    public class GetMeetingInvitationsRequest : Request
    {
    }

    public class GetMeetingInvitationsResponse
    {
        public List<Meeting> List { get; set; }
    }

    public enum UserEventStatus
    {
        NotAccepted = 0,
        Accepted = 1,
        Refused = 2
    }

    public class AddMeetingRequest : Request
    {
        public String Name { get; set; }
        public String LocationName { get; set; }
        public decimal Latitude { get; set; }
        public decimal Longitude { get; set; }
        public DateTime StartTime { get; set; }
        public DateTime EndTime { get; set; }
        public List<int> UsersList { get; set; }
    }

    public class AddMeetingResponse
    {
        public int IdMeeting { get; set; }
    }

    public class EditMeetingRequest : Request
    {
        public int IdMeeting { get; set; }
        public String Name { get; set; }
        public String LocationName { get; set; }
        public decimal Latitude { get; set; }
        public decimal Longitude { get; set; }
        public DateTime StartTime { get; set; }
        public DateTime EndTime { get; set; }
        public List<int> UsersList { get; set; }
    }

    public class EditMeetingResponse
    {
        public List<Meeting> List { get; set; }
    }
}