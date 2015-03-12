using LaggerServer.Database;
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
