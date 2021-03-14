-- commented because it's not supported on Azure as I created again on-premise SQL Server
---------------------------------------------- ------------------------------------
-- PRINT 'Creating Login user for demo_db'
-----------------------------------------------------------------------------------
--IF NOT EXISTS(SELECT * FROM sys.server_principals  WHERE name = N'demo_user')
--  BEGIN
--    CREATE LOGIN demo_user WITH PASSWORD = N'Password@1', DEFAULT_DATABASE = demo_db, DEFAULT_LANGUAGE =[us_english], CHECK_EXPIRATION = OFF, CHECK_POLICY = OFF
--  END
-- GO

USE demo_db
GO

---------------------------------------------- ------------------------------------
-- PRINT 'Creating Role and grant member access to demo_db'
-----------------------------------------------------------------------------------
--IF NOT EXISTS(SELECT * FROM sys.database_principals WHERE (name = N'demo_app') AND (type = N'R'))
--BEGIN
--CREATE ROLE demo_app
--CREATE USER demo_user FOR LOGIN demo_user
--ALTER ROLE demo_app ADD MEMBER demo_user
--END
--GO


---------------------------------------------- ------------------------------------
PRINT 'Creating Table For users If It Does Not Exist'
-----------------------------------------------------------------------------------
IF NOT EXISTS(SELECT *  FROM sys.objects  WHERE object_id = OBJECT_ID(N'users') AND type IN (N'U'))
BEGIN
CREATE TABLE users(
[id] BIGINT IDENTITY PRIMARY KEY,
[title] VARCHAR(20) NOT NULL,
[first_name] VARCHAR(50) NOT NULL,
[last_name] VARCHAR(50) NOT NULL,
[email] VARCHAR(75) NOT NULL,
[mobile] VARCHAR(16) NOT NULL,
[password] NTEXT NOT NULL,
[role] VARCHAR(5) NOT NULL,
[date_registered] DateTime NOT NULL DEFAULT GETDATE(),
[verification_code] VARCHAR(500) NULL,
[verified] BIT NOT NULL DEFAULT 0,
[date_verified] DateTime NULL,
[deactivated] BIT NOT NULL DEFAULT 0,
[date_deactivated] DateTime,
[date_last_updated] DATETIME
)
INSERT INTO users([title], [first_name],[last_name],[email],[mobile],[role],[verified],[date_verified],[password])
VALUES('Mr', 'John', 'Doe', 'john.doe@yahoo.com', '+2348083484930', 'ADMIN', 1, GETDATE(), '$2a$12$YD4YVu0u5yKDnvT7y9iwce9eVKMwlW4UHJArG57gsuQZDVr8pFEqy');
INSERT INTO users([title], [first_name],[last_name],[email],[mobile],[role],[verified],[date_verified],[password])
VALUES('Mr', 'Sammy', 'Tom', 'sammy.tom@yahoo.com', '+2348083484931', 'ADMIN', 1, GETDATE(), '$2a$12$YD4YVu0u5yKDnvT7y9iwce9eVKMwlW4UHJArG57gsuQZDVr8pFEqy');
INSERT INTO users([title], [first_name],[last_name],[email],[mobile],[role],[verified],[date_verified],[password])
VALUES('Mr', 'Solo', 'Ado', 'solo.ado@yahoo.com', '+2348083484932', 'USER', 1, GETDATE(), '$2a$12$YD4YVu0u5yKDnvT7y9iwce9eVKMwlW4UHJArG57gsuQZDVr8pFEqy');
INSERT INTO users([title], [first_name],[last_name],[email],[mobile],[role],[verified],[date_verified],[password])
VALUES('Mr', 'Kwame', 'Mensah', 'kwame.mensah@yahoo.com', '+2348083484933', 'USER', 1, GETDATE(), '$2a$12$YD4YVu0u5yKDnvT7y9iwce9eVKMwlW4UHJArG57gsuQZDVr8pFEqy');
END;

---------------------------------------------- ------------------------------------------------
PRINT 'Creating UNIQUE Index for email column for demo.users table if it does not exist'
-----------------------------------------------------------------------------------------------
IF NOT EXISTS(SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[demo].[users]') AND name = N'idx_users_email')
CREATE UNIQUE NONCLUSTERED INDEX idx_users_email ON users
(
[email]
)
WITH (ONLINE = ON)
GO

---------------------------------------------- ------------------------------------------------
PRINT 'Creating UNIQUE Index for mobile column for demo.users table if it does not exist'
-----------------------------------------------------------------------------------------------
IF NOT EXISTS(SELECT * FROM sys.indexes WHERE object_id = OBJECT_ID(N'[demo].[users]') AND name = N'idx_users_mobile')
CREATE UNIQUE NONCLUSTERED INDEX idx_users_mobile ON users
(
[mobile]
)
WITH (ONLINE = ON)
GO
