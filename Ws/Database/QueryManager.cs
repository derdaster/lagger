using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer.Database
{
    public static class QueryManager
    {
        public static Event GetEvent(LaggerDbEntities ctx, int idEvent)
        {
            return (from e in ctx.Events
                    where e.ID_Event == idEvent
                    && !e.Blocked
                    select e).FirstOrDefault();
        }

        public static List<int> GetUsersEvent(LaggerDbEntities ctx, int idEvent)
        {
            return (from ed in ctx.EventDetails
                    where ed.IDEvent == idEvent
                    && !ed.Blocked
                    select ed.IDUser).Distinct().ToList();
        }

        public static EventDetail GetLastUserPosition(LaggerDbEntities ctx, int idUser, int idEvent)
        {
            return (from ed in ctx.EventDetails
                    where ed.IDEvent == idEvent
                    && ed.IDUser == idUser
                    && !ed.Blocked
                    orderby ed.CreationDate descending
                    select ed).First();
        }

        public static List<EventDetail> GetUserPositionsFromDate(LaggerDbEntities ctx, int idUser, int idEvent, DateTime dateFrom)
        {
            return (from ed in ctx.EventDetails
                    where ed.IDEvent == idEvent
                    && ed.IDUser == idUser
                    && ed.CreationDate >= dateFrom
                    && !ed.Blocked
                    orderby ed.CreationDate
                    select ed).ToList();
        }
    }
}