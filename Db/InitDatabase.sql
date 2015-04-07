USE [Lagger]

-- Usuwanie wszystkich kluczy obcych

DECLARE @sql NVARCHAR(MAX) = ''

SELECT @sql += N'
ALTER TABLE ' + QUOTENAME(OBJECT_SCHEMA_NAME(parent_object_id))
    + '.' + QUOTENAME(OBJECT_NAME(parent_object_id)) + 
    ' DROP CONSTRAINT ' + QUOTENAME(name)
FROM sys.foreign_keys

PRINT @sql
EXEC(@sql)
GO



-- Usuwanie wszystkich kluczy g³ównych

DECLARE @sql NVARCHAR(MAX) = ''

SELECT @sql += '
ALTER TABLE ' + QUOTENAME(OBJECT_SCHEMA_NAME(parent_object_id))
    + '.' + QUOTENAME(OBJECT_NAME(parent_object_id)) + 
    ' DROP CONSTRAINT ' + QUOTENAME(name)
FROM sys.key_constraints

PRINT @sql
EXEC(@sql)
GO



-- Usuwanie wszystkich tabel

DECLARE @sql NVARCHAR(MAX) = ''

SELECT @sql += '
DROP TABLE ' + QUOTENAME(TABLE_NAME)
FROM INFORMATION_SCHEMA.TABLES

PRINT @sql
EXEC(@sql)
GO

-- User

CREATE TABLE [User]
(
	[ID_User] int NOT NULL IDENTITY(1,1),
	[Login] nvarchar(100) NOT NULL,
	[Password] nvarchar(100) NOT NULL,
	[Email] nvarchar(100) NULL,
	[Phone] nvarchar(20) NULL,
	[Activated] bit NOT NULL,
	[CreationDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[LastEditDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[Blocked] bit NOT NULL DEFAULT (0),
	CONSTRAINT [PK_User] PRIMARY KEY([ID_User])
)
GO



-- Event

CREATE TABLE [Event]
(
	[ID_Event] int NOT NULL IDENTITY(1,1),
	[Name] nvarchar(200) NOT NULL,
	[LocationName] nvarchar(500) NULL,
	[Latitude] decimal NOT NULL,
	[Longitude] decimal NOT NULL,
	[StartTime] datetime NOT NULL,
	[EndTime] datetime NOT NULL,
	[CreationDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[LastEditDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[Blocked] bit NOT NULL DEFAULT (0),
	CONSTRAINT [PK_Event] PRIMARY KEY([ID_Event])
)
GO



-- EventDetails

CREATE TABLE [EventDetails]
(
	[ID_EventDetails] int NOT NULL IDENTITY(1,1),
	[IDUser] int NOT NULL,
	[IDEvent] int NOT NULL,
	[Latitude] decimal(10,7) NOT NULL,
	[Longitude] decimal(10,7) NOT NULL,
	[CreationDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[LastEditDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[Blocked] bit NOT NULL DEFAULT (0),
	CONSTRAINT [PK_EventDetails] PRIMARY KEY([ID_EventDetails]),
	CONSTRAINT [FK_EventDetails_User] FOREIGN KEY ([IDUser]) REFERENCES [User]([ID_User]),
	CONSTRAINT [FK_EventDetails_Event] FOREIGN KEY ([IDEvent]) REFERENCES [Event]([ID_Event])
)
GO

-- UserEvent

CREATE TABLE [UserEvent]
(
	[ID_UserEvent] int NOT NULL IDENTITY(1,1),
	[IDUser] int NOT NULL,
	[IDEvent] int NOT NULL,
	[Status] smallint NOT NULL,
	[CreationDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[LastEditDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[Blocked] bit NOT NULL DEFAULT (0),
	CONSTRAINT [PK_UserEvent] PRIMARY KEY([ID_UserEvent]),
	CONSTRAINT [FK_UserEvent_User] FOREIGN KEY ([IDUser]) REFERENCES [User]([ID_User]),
	CONSTRAINT [FK_UserEvent_Event] FOREIGN KEY ([IDEvent]) REFERENCES [Event]([ID_Event])
)
GO

-- UserFriend

CREATE TABLE [UserFriend]
(
	[ID_UserFriend] int NOT NULL IDENTITY(1,1),
	[IDUser] int NOT NULL,
	[IDFriend] int NOT NULL,
	[Status] smallint NOT NULL,
	[CreationDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[LastEditDate] datetime NOT NULL DEFAULT GETUTCDATE(),
	[Blocked] bit NOT NULL DEFAULT (0),
	CONSTRAINT [PK_UserFriend] PRIMARY KEY([ID_UserFriend]),
	CONSTRAINT [FK_UserFriend_User] FOREIGN KEY ([IDUser]) REFERENCES [User]([ID_User]),
	CONSTRAINT [FK_UserFriend_Event] FOREIGN KEY ([IDFriend]) REFERENCES [User]([ID_User])
)
GO
