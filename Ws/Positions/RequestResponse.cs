using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer.Positions
{
    public class AddPositionRequest : Request
    {
        public int IdMeeting { get; set; }
        public decimal Latitude { get; set; }
        public decimal Longitude { get; set; }
        public DateTime DateTime { get; set; }
    }

    public class AddPositionResponse
    {
    }

    public class GetPositionsRequest : Request
    {
        public int IdMeeting { get; set; }
    }

    public class GetPositionsResponse
    {
        public List<Position> List { get; set; }
    }
}