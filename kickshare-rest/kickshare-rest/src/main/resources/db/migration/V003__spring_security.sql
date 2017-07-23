CREATE TABLE users (
    id BIGSERIAL REFERENCES backer (id) ON DELETE CASCADE,
	username VARCHAR (50) NOT NULL PRIMARY KEY,
	password VARCHAR (72) NOT NULL,
	user_created TIMESTAMP DEFAULT NOW(),
	token CHAR(36) NOT NULL,
	enabled BOOLEAN NOT NULL
);

CREATE TABLE authorities (
	username VARCHAR (50) NOT NULL,
	authority VARCHAR (50) NOT NULL,
	CONSTRAINT fk_authorities_users FOREIGN KEY(username) REFERENCES users(username)
);
CREATE UNIQUE INDEX ix_auth_username on authorities (username,authority);

CREATE TABLE groups (
	id BIGSERIAL PRIMARY KEY,
	group_name VARCHAR (50) NOT NULL
);

CREATE TABLE group_authorities (
	group_id BIGSERIAL NOT NULL,
	authority VARCHAR(50) NOT NULL,
	CONSTRAINT fk_group_authorities_group FOREIGN KEY(group_id) REFERENCES groups(id)
);

CREATE TABLE group_members (
	id BIGSERIAL PRIMARY KEY,
	username VARCHAR(50) NOT NULL,
	group_id BIGSERIAL NOT NULL,
	constraint fk_group_members_group FOREIGN KEY(group_id) REFERENCES groups(id)
);