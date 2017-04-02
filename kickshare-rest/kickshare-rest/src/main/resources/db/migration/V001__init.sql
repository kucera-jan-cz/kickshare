CREATE SCHEMA IF NOT EXISTS KICKSHARE;
SET SCHEMA KICKSHARE;

--@TODO deal with roles (reader, writer/admin)?

CREATE TABLE project (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY,
	name varchar(255) not null,
	description varchar(255) not null,
	url varchar(255) not null,
	deadline DATE
);

CREATE TABLE user (
	id BIGINT GENERATED BY DEFAULT AS IDENTITY,
	email varchar (255),
	name varchar (255) not null,
	surname varchar (255) not null
);

CREATE TABLE `group` (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY,
    leader_id BIGINT not null,
    name varchar (255) not null,
    CONSTRAINT fk_user_id FOREIGN KEY(leader_id) REFERENCES user(id)
);

CREATE TABLE user_2_group (
    group_id BIGINT,
    user_id BIGINT
);
