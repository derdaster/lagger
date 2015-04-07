using LaggerServer.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;
using System.Web.Script.Serialization;

namespace LaggerServer
{
    public class Request
    {
        [DataMember(Name = "idUser")]
        public int IdUser { get; set; }
    }

    #region LoginUser

    [DataContract]
    public class LoginUserRequest
    {
        [DataMember(Name = "login")]
        public String Login { get; set; }

        [DataMember(Name = "password")]
        public String Password { get; set; }
    }

    [DataContract]
    public class LoginUserResponse
    {
        [DataMember(Name = "idUser")]
        public int IdUser { get; set; }

        [DataMember(Name = "status")]
        public LoginUserStatus Status { get; set; }
    }

    [DataContract]
    public enum LoginUserStatus
    {
        [EnumMember]
        UnregisteredUser,
        [EnumMember]
        Success,
        [EnumMember]
        IncorrectPassword
    }

    #endregion

    #region GetMettings

    public class GetMeetingsRequest : Request
    {
    }

    public class GetMeetingsResponse
    {
        public List<Meeting> List { get; set; }
    }

    #endregion

    #region GetFriends

    public class GetFriendsRequest : Request
    {
    }

    public class GetFriendsResponse
    {
        public List<Friend> List { get; set; }
    }

    public enum UserFriendStatus
    {
        NotAccepted = 0,
        Accepted = 1,
        Refused = 2
    }

    #endregion

    #region AddFriend

    public class AddFriendRequest : Request
    {
        public int IdFriend { get; set; }
    }

    public class AddFriendResponse
    {

    }

    #endregion

    #region RemoveFriend

    public class RemoveFriendRequest : Request
    {
        public int IdFriend { get; set; }
    }

    public class RemoveFriendResponse
    {

    }

    #endregion

    #region GetMeetingInvitations

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

    #endregion

    #region AddMeeting

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

    #endregion

    #region EditMeeting

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

    #endregion
}