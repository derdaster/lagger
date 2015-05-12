using LaggerServer.Database;
using LaggerServer.Friends;
using LaggerServer.Utils;
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
            try
            {
                LogDiagnostic("GetFriends", request.IdUser);

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
                        Friends = list.Union(list2).OrderBy(x => x.Login).ToList()
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("GetFriends", ex);
                return null;
            }
        }

        public GetFriendInvitationsResponse GetFriendInvitations(GetFriendInvitationsRequest request)
        {
            try
            {
                LogDiagnostic("GetFriendInvitations", request.IdUser);

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
                        FriendInvitations = list.OrderBy(x => x.Login).ToList()
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("GetFriendInvitations", ex);
                return null;
            }
        }

        public AddFriendResponse AddFriend(AddFriendRequest request)
        {
            try
            {
                LogDiagnostic("AddFriend", request.IdUser);

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
                            Status = (short)UserFriendStatus.NotAccepted,
                            LastEditDate = DateTime.UtcNow,
                            CreationDate = DateTime.UtcNow,
                            Blocked = false
                        };

                        ctx.UserFriends.InsertOnSubmit(entity);
                    }

                    ctx.SubmitChanges();

                    return new AddFriendResponse();
                }
            }
            catch (Exception ex)
            {
                SetError("AddFriend Error", ex);
                return null;
            }
        }

        public RemoveFriendResponse RemoveFriend(RemoveFriendRequest request)
        {
            try
            {
                LogDiagnostic("RemoveFriend", request.IdUser);

                using (var ctx = new LaggerDbEntities())
                {
                    var entity = (from uf in ctx.UserFriends
                                  where (uf.Status == (short)UserFriendStatus.NotAccepted
                                  || uf.Status == (short)UserFriendStatus.Accepted)
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
                            Status = (short)UserFriendStatus.Refused,
                            LastEditDate = DateTime.UtcNow,
                            CreationDate = DateTime.UtcNow,
                            Blocked = false
                        };

                        ctx.UserFriends.InsertOnSubmit(entity);
                    }

                    ctx.SubmitChanges();

                    return new RemoveFriendResponse();
                }
            }
            catch (Exception ex)
            {
                SetError("RemoveFriend Error", ex);
                return null;
            }
        }


        public FindFriendResponse FindFriend(FindFriendRequest request)
        {
            try
            {
                LogDiagnostic("FindFriend", request.IdUser);

                if (request.Pattern == null || request.Pattern.Count() < 3)
                {
                    throw new Exception("Fraza wyszukiwania musi mieć przynajmniej 3 znaki");
                }

                using (var ctx = new LaggerDbEntities())
                {
                    var list = from uf in ctx.UserFriends
                               join u in ctx.Users
                               on uf.IDUser equals u.ID_User
                               where uf.Status == (short)UserFriendStatus.Accepted
                               && uf.IDFriend == request.IdUser
                               && !u.Blocked
                               && !uf.Blocked
                               select u.ID_User;

                    var list2 = from uf in ctx.UserFriends
                                join u in ctx.Users
                                on uf.IDFriend equals u.ID_User
                                where uf.Status == (short)UserFriendStatus.Accepted
                                && uf.IDUser == request.IdUser
                                && !u.Blocked
                                && !uf.Blocked
                                select u.ID_User;

                    var friends = list.Union(list2);

                    var allUsers = (from u in ctx.Users
                                    where u.ID_User != request.IdUser
                                    && !u.Blocked
                                    && (u.Login.Contains(request.Pattern) || u.Email.Contains(request.Pattern))
                                    select u);

                    var usersList = allUsers.Where(x => !friends.Contains(x.ID_User))
                        .Take(10).OrderBy(x => x.Login).Select(x => new Friend(x)).ToList();

                    return new FindFriendResponse()
                    {
                        Users = usersList
                    };
                }
            }
            catch (Exception ex)
            {
                SetError("FindFriend Error", ex);
                return null;
            }
        }
    }
}