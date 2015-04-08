using LaggerServer.Database;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;
using System.Web.Script.Serialization;

namespace LaggerServer
{
    [DataContract]
    public class Request
    {
        [DataMember(Name = "idUser", Order = 0)]
        public int IdUser { get; set; }
    }

    [DataContract]
    public class Response
    {
    }

    #region LoginUser

    [DataContract]
    public class LoginUserRequest
    {
        [DataMember(Name = "login", Order = 0)]
        public String Login { get; set; }

        [DataMember(Name = "password", Order = 1)]
        public String Password { get; set; }
    }

    [DataContract]
    public class LoginUserResponse : Response
    {
        [DataMember(Name = "idUser", Order = 0)]
        public int IdUser { get; set; }

        [DataMember(Name = "status", Order = 1)]
        public LoginUserStatus Status { get; set; }
    }

    [DataContract]
    public enum LoginUserStatus
    {
        [EnumMember]
        UnregisteredUser = 0,

        [EnumMember]
        Success = 1,

        [EnumMember]
        IncorrectPassword = 2
    }

    #endregion

}