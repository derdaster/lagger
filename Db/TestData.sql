SET IDENTITY_INSERT [User] ON

INSERT INTO [User] ([ID_User], [Login], [Password], [Email], [Activated], [CreationDate], [LastEditDate], [Blocked])
VALUES (1, 'Test', 'tajneHaslo', 'pl@pl.pl', 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT INTO [User] ([ID_User], [Login], [Password], [Email], [Activated], [CreationDate], [LastEditDate], [Blocked])
VALUES (2, 'anilewek', 'ani', 'anilewek@gmail.com', 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT INTO [User] ([ID_User], [Login], [Password], [Email], [Activated], [CreationDate], [LastEditDate], [Blocked])
VALUES (3, 'kamiss', 'kam', 'kamisek6pl@gmail.com', 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT INTO [User] ([ID_User], [Login], [Password], [Email], [Activated], [CreationDate], [LastEditDate], [Blocked])
VALUES (4, 'derdaster', 'der', 'derdaster@gmail.com', 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT INTO [User] ([ID_User], [Login], [Password], [Email], [Activated], [CreationDate], [LastEditDate], [Blocked])
VALUES (5, 'kanoe92', 'kan', 'kanoe92@gmail.com', 1, GETUTCDATE(), GETUTCDATE(), 0)

SET IDENTITY_INSERT [User] OFF
GO




SET IDENTITY_INSERT [Event] ON

INSERT [dbo].[Event] ([ID_Event], [Name], [LocationName], [Latitude], [Longitude], [StartTime], [EndTime], [CreationDate], [LastEditDate], [Blocked]) VALUES (1, N'Impreza u Maæka', N'Pub Pod Dêbem', CAST(51 AS Decimal(18, 0)), CAST(17 AS Decimal(18, 0)), CAST(N'2015-03-20 19:00:00.000' AS DateTime), CAST(N'2015-03-20 22:00:00.000' AS DateTime), GETUTCDATE(), GETUTCDATE(), 0)

SET IDENTITY_INSERT [Event] OFF
GO




SET IDENTITY_INSERT [UserEvent] ON

INSERT [dbo].[UserEvent] ([ID_UserEvent], [IDUser], [IDEvent], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (1, 1, 1, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserEvent] ([ID_UserEvent], [IDUser], [IDEvent], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (2, 2, 1, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserEvent] ([ID_UserEvent], [IDUser], [IDEvent], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (3, 3, 1, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserEvent] ([ID_UserEvent], [IDUser], [IDEvent], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (4, 4, 1, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserEvent] ([ID_UserEvent], [IDUser], [IDEvent], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (5, 5, 1, 1, GETUTCDATE(), GETUTCDATE(), 0)

SET IDENTITY_INSERT [UserEvent] OFF
GO




SET IDENTITY_INSERT [UserFriend] ON

INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (1, 1, 2, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (2, 1, 3, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (3, 4, 1, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (4, 5, 1, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (5, 2, 3, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (6, 2, 4, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (7, 4, 5, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (8, 3, 4, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (9, 5, 3, 1, GETUTCDATE(), GETUTCDATE(), 0)
INSERT [dbo].[UserFriend] ([ID_UserFriend], [IDUser], [IDFriend], [Status], [CreationDate], [LastEditDate], [Blocked]) VALUES (10, 5, 2, 1, GETUTCDATE(), GETUTCDATE(), 0)

SET IDENTITY_INSERT [UserFriend] OFF
GO