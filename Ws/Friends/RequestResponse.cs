using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer.Friends
{
    public class GetFriendsRequest : Request
    {
    }

    public class GetFriendsResponse
    {
        public List<Friend> List { get; set; }
    }

    public class GetFriendInvitationsRequest : Request
    {
    }

    public class GetFriendInvitationsResponse
    {
        public List<Friend> List { get; set; }
    }

    public enum UserFriendStatus
    {
        NotAccepted = 0,
        Accepted = 1,
        Refused = 2
    }

    public class AddFriendRequest : Request
    {
        public int IdFriend { get; set; }
    }

    public class AddFriendResponse
    {

    }

    public class RemoveFriendRequest : Request
    {
        public int IdFriend { get; set; }
    }

    public class RemoveFriendResponse
    {

    }
}