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

        [DataMember(Name = "dateTime")]
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
    }

    [DataContract]
    public class GetPositionsResponse : Response
    {
        [DataMember(Name = "positions", Order = 0)]
        public List<Position> Positions { get; set; }
    }
}