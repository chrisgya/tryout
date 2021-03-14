
-----------------------------------------------------------------------
---											proc_get_users															---
-----------------------------------------------------------------------

IF EXISTS(SELECT *  FROM   sys.objects WHERE  object_id = Object_id(N'proc_get_users') AND type IN ( N'P', N'PC' ))
DROP PROCEDURE [dbo].proc_get_users
GO
CREATE PROCEDURE [dbo].proc_get_users
(
@page_number	INT = 0,
@page_size  INT = 20
)
AS
SET NOCOUNT ON

IF @page_number < 1
SET @page_number = 1
IF @page_size < 1
SET @page_size = 20
DECLARE @offset INT = (@page_number - 1) * @page_size

SELECT COUNT(*) AS [count] FROM users;

SELECT
[id],
[title],
[first_name],
[last_name],
[email],
[mobile],
[password],
[role],
[date_registered],
[verified],
[date_verified],
[date_deactivated],
CASE
WHEN deactivated=1 THEN 'DEACTIVATED'
WHEN [verified]=1 THEN 'VERIFIED'
ELSE 'REGISTERED'
END AS [status]
FROM users
ORDER BY id OFFSET @offset ROWS FETCH NEXT @page_size ROWS ONLY;

GO
--GRANT  EXECUTE  ON proc_get_users TO demo_user;
--GO


-----------------------------------------------------------------------
---											proc_get_user 															---
-----------------------------------------------------------------------

IF EXISTS(SELECT *  FROM   sys.objects WHERE  object_id = Object_id(N'proc_get_user') AND type IN ( N'P', N'PC' ))
DROP PROCEDURE [dbo].proc_get_user
GO
CREATE PROCEDURE [dbo].proc_get_user
(
@id BIGINT
)
AS
SET NOCOUNT ON

SELECT
[id],
[title],
[first_name],
[last_name],
[email],
[mobile],
[password],
[role],
[date_registered],
[verified],
[date_verified],
[date_deactivated],
CASE
WHEN deactivated=1 THEN 'DEACTIVATED'
WHEN [verified]=1 THEN 'VERIFIED'
ELSE 'REGISTERED'
END AS [status]
FROM users
WHERE id=@id;

GO
--GRANT  EXECUTE  ON proc_get_user TO demo_user;
--GO

-----------------------------------------------------------------------
---											proc_create_user														---
-----------------------------------------------------------------------

IF EXISTS(SELECT *  FROM  sys.objects WHERE  object_id = Object_id(N'proc_create_user') AND type IN ( N'P', N'PC' ))
DROP PROCEDURE [dbo].proc_create_user
GO
CREATE PROCEDURE [dbo].proc_create_user
(
@title VARCHAR(20),
@first_name VARCHAR(50),
@last_name VARCHAR(50),
@email VARCHAR(75),
@mobile VARCHAR(16),
@password NTEXT,
@role VARCHAR(5),
@verification_code VARCHAR(500)
)
AS

INSERT INTO users([title], [first_name],[last_name],[email],[mobile],[role],[verification_code],[password])
VALUES(@title, @first_name, @last_name, @email, @mobile, @role, @verification_code, @password);

SELECT
[id],
[title],
[first_name],
[last_name],
[email],
[mobile],
[password],
[role],
[date_registered],
[verified],
[date_verified],
[date_deactivated],
'REGISTERED' AS [status]
FROM users
WHERE id = SCOPE_IDENTITY();

GO
--GRANT  EXECUTE  ON proc_create_user TO demo_user;
--GO



-----------------------------------------------------------------------
---											proc_update_user														---
-----------------------------------------------------------------------

IF EXISTS(SELECT *  FROM  sys.objects WHERE  object_id = Object_id(N'proc_update_user') AND type IN ( N'P', N'PC' ))
DROP PROCEDURE [dbo].proc_update_user
GO
CREATE PROCEDURE [dbo].proc_update_user
(
@id BIGINT,
@title VARCHAR(20),
@first_name VARCHAR(50),
@last_name VARCHAR(50),
@mobile VARCHAR(16),
@role VARCHAR(5),
@RETURN_VALUE [INT] OUTPUT
)
AS

UPDATE users SET
[title] = @title,
[first_name]=@first_name,
[last_name]=@last_name,
[mobile]=@mobile,
[role]=@role,
date_last_updated = GETDATE()
WHERE id=@id;

SELECT @RETURN_VALUE = @@ROWCOUNT;

GO
--GRANT  EXECUTE  ON proc_update_user TO demo_user;
--GO


---------------------------------------------------------------------------
---											proc_deactivate_user														---
---------------------------------------------------------------------------

IF EXISTS(SELECT *  FROM  sys.objects WHERE  object_id = Object_id(N'proc_deactivate_user') AND type IN ( N'P', N'PC' ))
DROP PROCEDURE [dbo].proc_deactivate_user
GO
CREATE PROCEDURE [dbo].proc_deactivate_user
(
@id BIGINT,
@RETURN_VALUE [INT] OUTPUT
)
AS

IF EXISTS(SELECT 1 FROM users WHERE id=@id AND deactivated=1)
THROW 51000, 'user already deactivated', 1;

UPDATE users SET
[deactivated] = 1,
[date_deactivated] = GETDATE()
WHERE id=@id;

SELECT @RETURN_VALUE = @@ROWCOUNT;

GO
--GRANT  EXECUTE  ON proc_deactivate_user TO demo_user;
--GO


-----------------------------------------------------------------------
---											proc_verify_user														---
-----------------------------------------------------------------------

IF EXISTS(SELECT *  FROM  sys.objects WHERE  object_id = Object_id(N'proc_verify_user') AND type IN ( N'P', N'PC' ))
DROP PROCEDURE [dbo].proc_verify_user
GO
CREATE PROCEDURE [dbo].proc_verify_user
(
@verification_code VARCHAR(500),
@RETURN_VALUE [INT] OUTPUT
)
AS

IF NOT EXISTS(SELECT 1 FROM users WHERE verification_code=@verification_code)
THROW 51000, 'verification code not found', 1;

IF EXISTS(SELECT 1 FROM users WHERE verification_code=@verification_code AND verified=1)
THROW 51000, 'user already verified', 1;

UPDATE users SET
verified=1,
[date_verified] = GETDATE()
WHERE verification_code=@verification_code;

SELECT @RETURN_VALUE = @@ROWCOUNT;

GO
--GRANT  EXECUTE  ON proc_verify_user TO demo_user;
--GO













