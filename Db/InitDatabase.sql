DROP TABLE [User]

CREATE TABLE [User]
(
	[ID_User] int NOT NULL,
	[Login] nvarchar(100) NOT NULL,
	[Password] nvarchar(100) NOT NULL,
	[Email] nvarchar(100) NULL,
	[Activated] bit NOT NULL,
	[CreationDate] datetime NOT NULL,
	[LastEditDate] datetime NOT NULL,
	[Blocked] bit NOT NULL
)

ALTER TABLE [User]
ADD CONSTRAINT PK_User PRIMARY KEY([ID_User])

