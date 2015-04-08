using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace LaggerServer.Friends
{
    [DataContract]
    public class GetFriendsRequest : Request
    {
    }

    [DataContract]
    public class GetFriendsResponse : Response
    {
        [DataMember(Name = "friends", Order = 0)]
        public List<Friend> Friends { get; set; }
    }

    [DataContract]
    public class GetFriendInvitationsRequest : Request
    {
    }

    [DataContract]
    public class GetFriendInvitationsResponse : Response
    {
        [DataMember(Name = "friendInvitations", Order = 0)]
        public List<Friend> FriendInvitations { get; set; }
    }

    public enum UserFriendStatus
    {
        NotAccepted = 0,
        Accepted = 1,
        Refused = 2
    }

    [DataContract]
    public class AddFriendRequest : Request
    {
        [DataMember(Name = "idFriend", Order = 0)]
        public int IdFriend { get; set; }
    }

    [DataContract]
    public class AddFriendResponse : Response
    {

    }

    [DataContract]
    public class RemoveFriendRequest : Request
    {
        [DataMember(Name = "idFriend", Order = 0)]
        public int IdFriend { get; set; }
    }

    [DataContract]
    public class RemoveFriendResponse : Response
    {

    }
}