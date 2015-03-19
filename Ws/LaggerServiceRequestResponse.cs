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
        public int IdUser { get; set; }
    }

    #region LoginUser

    public class LoginUserRequest
    {
        public String Login { get; set; }
        public String Password { get; set; }
    }

    public class LoginUserResponse
    {
        public int IdUser { get; set; }

        public LoginUserStatus Status { get; set; }
    }

    public enum LoginUserStatus
    {
        UnregisteredUser,
        Success,
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
}