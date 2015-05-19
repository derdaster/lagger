using LaggerServer.Database;
using LaggerServer.Positions;
using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlTypes;
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

                if (request.LastRequestTime < SqlDateTime.MinValue.Value) { request.LastRequestTime = SqlDateTime.MinValue.Value; }

                var predictionTime = 5;

                using (var ctx = new LaggerDbEntities())
                {
                    var allUsersPositions = new List<PositionDetails>();

                    var direction = QueryManager.GetEvent(ctx, request.IdMeeting);

                    if (direction == null) { throw new Exception("Nie ma takiego spotkania"); }

                    var users = QueryManager.GetUsersEvent(ctx, request.IdMeeting);

                    foreach (var idUser in users)
                    {
                        var lastPosition = QueryManager.GetLastUserPosition(ctx, idUser, request.IdMeeting);

                        var datePrediction = lastPosition.CreationDate.AddMinutes(-1 * predictionTime);
                        var dateFrom = (request.LastRequestTime < datePrediction) ? request.LastRequestTime : datePrediction;

                        var userPositions = QueryManager.GetUserPositionsFromDate(ctx, idUser, request.IdMeeting, dateFrom);

                        var userPositionsPrediction = userPositions.Where(x => x.CreationDate >= datePrediction)
                                                                 .OrderBy(x => x.CreationDate);

                        int arrivalTime = 0;
                        var positionsList = new List<Position>();

                        if (userPositionsPrediction.Count() == 1)
                        {
                            arrivalTime = -1; // Nieznany czas dotarcia. Jest tylko jedna pozycja. Stoi w miejscu i dopiero zaczął przygodę.
                        }
                        else if (userPositionsPrediction.Count() > 1)
                        {
                            if (IsDestination(direction, userPositionsPrediction.Last()))
                            {
                                arrivalTime = 0; // Jest na miejscu. Nie trzeba liczyć czasu dotarcia.
                            }
                            else
                            {
                                arrivalTime = GetArrivalTime(direction, userPositionsPrediction);
                            }
                        }

                        allUsersPositions.Add(new PositionDetails()
                           {
                               IdUser = idUser,
                               ArrivalTime = arrivalTime,
                               Positions = userPositions.Where(x => x.CreationDate > request.LastRequestTime).Select(x => new Position(x)).ToList()
                           });
                    }

                    return new GetPositionsResponse()
                    {
                        UsersPositions = allUsersPositions
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("GetPositions", ex);
                return null;
            }
        }

        private bool IsDestination(Event meeting, EventDetail position)
        {
            var latitude = (double)Math.Abs(meeting.Latitude - position.Latitude);
            var longitude = (double)Math.Abs(meeting.Longitude - position.Longitude);

            var leftMeters = (new GeoCoordinate(0, 0)).GetDistanceTo(new GeoCoordinate(latitude, longitude));

            return leftMeters < 100;
        }

        private int GetArrivalTime(Event direction, IOrderedEnumerable<EventDetail> userPositionsPrediction)
        {
            var distance = 0d;
            var time = new TimeSpan();

            var tempPosition = userPositionsPrediction.First();

            foreach (var pos in userPositionsPrediction)
            {
                distance += (new GeoCoordinate((double)tempPosition.Latitude, (double)tempPosition.Longitude))
                    .GetDistanceTo(new GeoCoordinate((double)pos.Latitude, (double)pos.Longitude));

                time += pos.CreationDate - tempPosition.CreationDate;

                tempPosition = pos;
            }

            var leftLat = (double)Math.Abs(tempPosition.Latitude - direction.Latitude);
            var leftLon = (double)Math.Abs(tempPosition.Longitude - direction.Longitude);

            var leftMeters = (new GeoCoordinate(0, 0)).GetDistanceTo(new GeoCoordinate(leftLat, leftLon));

            var velocity = distance / (double)time.TotalMinutes;

            return (velocity != 0) ? (int)Math.Round(leftMeters / velocity) : -1;
        }
    }
}