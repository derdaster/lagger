using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.Web;

namespace LaggerServer.Positions
{
    [DataContract]
    public class AddPositionRequest : Request
    {
        [DataMember(Name = "idMeeting", Order = 0)]
        public int IdMeeting { get; set; }

        [DataMember(Name = "latitude", Order = 1)]
        public decimal Latitude { get; set; }

        [DataMember(Name = "longitude", Order = 2)]
        public decimal Longitude { get; set; }

        [DataMember(Name = "dateTime", Order = 3)]
        public DateTime DateTime { get; set; }
    }

    [DataContract]
    public class AddPositionResponse : Response
    {
    }

    [DataContract]
    public class GetPositionsRequest : Request
    {
        [DataMember(Name = "idMeeting", Order = 0)]
        public int IdMeeting { get; set; }

        [DataMember(Name = "lastRequestTime", Order = 1)]
        public DateTime LastRequestTime { get; set; }
    }

    [DataContract]
    public class GetPositionsResponse : Response
    {
        [DataMember(Name = "usersPositions", Order = 0)]
        public List<PositionDetails> UsersPositions { get; set; }
    }
}