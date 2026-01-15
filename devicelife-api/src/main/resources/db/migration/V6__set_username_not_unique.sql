-- username UNIQUE 해제
ALTER TABLE users
DROP INDEX uk_users_username;