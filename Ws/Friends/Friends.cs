using LaggerServer.Database;
using LaggerServer.Friends;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LaggerServer
{
    public partial class LaggerService : ILaggerService
    {
        public GetFriendsResponse GetFriends(GetFriendsRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var list = from uf in ctx.UserFriends
                           join u in ctx.Users
                           on uf.IDUser equals u.ID_User
                           where uf.Status == (short)UserFriendStatus.Accepted
                           && uf.IDFriend == request.IdUser
                           && !u.Blocked
                           && !uf.Blocked
                           select new Friend(u);

                var list2 = from uf in ctx.UserFriends
                            join u in ctx.Users
                            on uf.IDFriend equals u.ID_User
                            where uf.Status == (short)UserFriendStatus.Accepted
                            && uf.IDUser == request.IdUser
                            && !u.Blocked
                            && !uf.Blocked
                            select new Friend(u);

                return new GetFriendsResponse()
                {
                    List = list.Union(list2).ToList()
                };
            }
        }

        public GetFriendInvitationsResponse GetFriendInvitations(GetFriendInvitationsRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var list = from uf in ctx.UserFriends
                           join u in ctx.Users
                           on uf.IDUser equals u.ID_User
                           where uf.Status == (short)UserFriendStatus.NotAccepted
                           && uf.IDFriend == request.IdUser
                           && !u.Blocked
                           && !uf.Blocked
                           select new Friend(u);

                return new GetFriendInvitationsResponse()
                {
                    List = list.ToList()
                };
            }
        }

        public AddFriendResponse AddFriend(AddFriendRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var entity = (from uf in ctx.UserFriends
                              where uf.Status == (short)UserFriendStatus.NotAccepted
                              && (uf.IDUser == request.IdUser && uf.IDFriend == request.IdFriend
                              || uf.IDFriend == request.IdUser && uf.IDUser == request.IdFriend)
                              && !uf.Blocked
                              select uf).FirstOrDefault();

                if (entity != null)
                {
                    entity.Status = (short)UserFriendStatus.Accepted;
                }
                else
                {
                    entity = new UserFriend()
                    {
                        IDUser = request.IdUser,
                        IDFriend = request.IdFriend,
                        Status = (short)UserFriendStatus.NotAccepted
                    };

                    ctx.UserFriends.InsertOnSubmit(entity);
                }

                ctx.SubmitChanges();

                return new AddFriendResponse();
            }
        }

        public RemoveFriendResponse RemoveFriend(RemoveFriendRequest request)
        {
            using (var ctx = new LaggerDbEntities())
            {
                var entity = (from uf in ctx.UserFriends
                              where uf.Status == (short)UserFriendStatus.NotAccepted
                              && (uf.IDUser == request.IdUser && uf.IDFriend == request.IdFriend
                              || uf.IDFriend == request.IdUser && uf.IDUser == request.IdFriend)
                              && !uf.Blocked
                              select uf).FirstOrDefault();

                if (entity != null)
                {
                    entity.Status = (short)UserFriendStatus.Refused;
                }
                else
                {
                    entity = new UserFriend()
                    {
                        IDUser = request.IdUser,
                        IDFriend = request.IdFriend,
                        Status = (short)UserFriendStatus.Refused
                    };

                    ctx.UserFriends.InsertOnSubmit(entity);
                }

                ctx.SubmitChanges();

                return new RemoveFriendResponse();
            }
        }
    }
}