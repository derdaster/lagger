using LaggerServer.Database;
using LaggerServer.Friends;
using LaggerServer.Meetings;
using LaggerServer.Positions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace LaggerServer
{
    [ServiceContract]
    public interface ILaggerService
    {
        [OperationContract]
        [ServiceKnownType(typeof(LoginUserStatus))]
        [WebInvoke(UriTemplate = "user/login", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        LoginUserResponse LoginUser(LoginUserRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/meetings/get", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        GetMeetingsResponse GetMeetings(GetMeetingsRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/meetings/invitation", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        GetMeetingInvitationsResponse GetMeetingInvitations(GetMeetingInvitationsRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/meetings/invitation/accept", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        MeetingInvitationAcceptResponse MeetingInvitationAccept(MeetingInvitationAcceptRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/meetings/add", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        AddMeetingResponse AddMeeting(AddMeetingRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/meetings/edit", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        EditMeetingResponse EditMeeting(EditMeetingRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/meetings/remove", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        RemoveMeetingResponse RemoveMeeting(RemoveMeetingRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/friends/get", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        GetFriendsResponse GetFriends(GetFriendsRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/friends/invitation", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        GetFriendInvitationsResponse GetFriendInvitations(GetFriendInvitationsRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/friends/add", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        AddFriendResponse AddFriend(AddFriendRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/friends/remove", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        RemoveFriendResponse RemoveFriend(RemoveFriendRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/friends/find", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        FindFriendResponse FindFriend(FindFriendRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/positions/add", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        AddPositionResponse AddPosition(AddPositionRequest request);

        [OperationContract]
        [WebInvoke(UriTemplate = "user/positions/get", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        GetPositionsResponse GetPositions(GetPositionsRequest request);


        #region Testy

        [OperationContract]
        [WebGet(UriTemplate = "test/connection", ResponseFormat = WebMessageFormat.Json)]
        string TestConnection();

        [OperationContract]
        [WebGet(UriTemplate = "test/connection/{value}", ResponseFormat = WebMessageFormat.Json)]
        string TestConnectionValue(string value);

        [OperationContract]
        [WebGet(UriTemplate = "test/json", ResponseFormat = WebMessageFormat.Json)]
        TestowaKlasa TestJsonGet();

        [OperationContract]
        [WebInvoke(UriTemplate = "test/json", Method = "POST", RequestFormat = WebMessageFormat.Json, ResponseFormat = WebMessageFormat.Json)]
        TestowaKlasa TestJsonPost(TestowaKlasa value);

        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Json, UriTemplate = "Test/{id}")]
        User GetUser(string id);

        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Json, UriTemplate = "test/resetInvitation/{id}")]
        String ResetInvitation(string id);

        [OperationContract]
        [WebGet(ResponseFormat = WebMessageFormat.Xml, UriTemplate = "test/showMeetings/{id}")]
        String ShowMeetings(string id);

        #endregion
    }


    [DataContract]
    public class TestowaKlasa
    {
        [DataMember]
        public String Login { get; set; }

        [DataMember]
        public String Password { get; set; }
    }
}
