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

}