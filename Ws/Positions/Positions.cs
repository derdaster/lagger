using LaggerServer.Database;
using LaggerServer.Positions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer
{
    public partial class LaggerService : ILaggerService
    {
        public AddPositionResponse AddPosition(AddPositionRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                ctx.EventDetails.InsertOnSubmit(new EventDetail
                    {
                        IDEvent = request.IdMeeting,
                        IDUser = request.IdUser,
                        Latitude = request.Latitude,
                        Longitude = request.Longitude,
                        CreationDate = DateTime.UtcNow,
                        LastEditDate = DateTime.UtcNow,
                        Blocked = false
                    });

                ctx.SubmitChanges();

                return new AddPositionResponse();
            }
        }

        public GetPositionsResponse GetPositions(GetPositionsRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var list = (from ed in ctx.EventDetails
                            where ed.IDEvent == request.IdMeeting
                            && !ed.Blocked
                            select new Position(ed)).ToList();

                return new GetPositionsResponse()
                {
                    List = list
                };
            }
        }
    }
}