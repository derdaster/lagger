using LaggerServer.Database;
using LaggerServer.Positions;
using System;
using System.Collections.Generic;
using System.Device.Location;
using System.Linq;
using System.ServiceModel.Web;
using System.Web;

namespace LaggerServer
{
    public partial class LaggerService : ILaggerService
    {
        public AddPositionResponse AddPosition(AddPositionRequest request)
        {
            try
            {
                LogDiagnostic("AddPosition", request.IdUser);

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
            catch (Exception ex)
            {
                SetError("AddPosition Error", ex);
                return null;
            }
        }

        public GetPositionsResponse GetPositions(GetPositionsRequest request)
        {
            try
            {
                LogDiagnostic("GetPositions", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var usersPositions = new List<PositionDetails>();

                    var direction = (from e in ctx.Events
                                     where e.ID_Event == request.IdMeeting
                                     && !e.Blocked
                                     select e).FirstOrDefault();

                    var eventDetails = (from ed in ctx.EventDetails
                                        where ed.IDEvent == request.IdMeeting
                                        && !ed.Blocked
                                        select ed).ToList();

                    var users = eventDetails.Select(x => x.IDUser).Distinct();

                    foreach (var u in users)
                    {
                        var positions = eventDetails.Where(x => x.IDUser == u).OrderBy(x => x.CreationDate);

                        if (positions.Any())
                        {
                            var distance = 0d;
                            var time = new TimeSpan();

                            var lastPosition = positions.First();

                            foreach (var pos in positions)
                            {
                                distance += (new GeoCoordinate((double)lastPosition.Latitude, (double)lastPosition.Longitude))
                                    .GetDistanceTo(new GeoCoordinate((double)pos.Latitude, (double)pos.Longitude));

                                time += pos.CreationDate - lastPosition.CreationDate;

                                lastPosition = pos;
                            }

                            var leftLat = (double)Math.Abs(lastPosition.Latitude - direction.Latitude);
                            var leftLon = (double)Math.Abs(lastPosition.Longitude - direction.Longitude);

                            var leftMeters = (new GeoCoordinate(0, 0)).GetDistanceTo(new GeoCoordinate(leftLat, leftLon));

                            var velocity = distance / (double)time.TotalMinutes;

                            var arrivalTime = (int)Math.Round(leftMeters / velocity);

                            usersPositions.Add(new PositionDetails()
                            {
                                IdUser = u,
                                ArrivalTime = arrivalTime,
                                Positions = positions.Select(x => new Position(x)).ToList()
                            });
                        }
                    }

                    return new GetPositionsResponse()
                    {
                        UsersPositions = usersPositions
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("GetPositions", ex);
                return null;
            }
        }
    }
}