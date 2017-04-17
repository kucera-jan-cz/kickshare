--@TODO deal with roles (reader, writer/admin)?

CREATE TABLE project (
	id bigserial PRIMARY KEY,
	name varchar(255) NOT NULL,
	description varchar(255) NOT NULL,
	url varchar(512) NOT NULL,
	deadline DATE
);

CREATE TABLE project_photo (
    project_id bigserial PRIMARY KEY REFERENCES project (id),
    thumb varchar(512),
	small varchar(512)
--	FOREIGN KEY (project_id) REFERENCES project (id)
--  CONSTRAINT fk_project_photo_project_id foreign key (project_id)
);

---- @TODO - email must be unique
CREATE TABLE "user" (
	id bigserial PRIMARY KEY,
	email varchar (255),
	name varchar (255) NOT NULL,
	surname varchar (255) NOT NULL
);

CREATE TABLE "group" (
    id bigserial PRIMARY KEY,
    leader_id BIGINT NOT NULL REFERENCES "user"(id),
    project_id BIGINT NOT NULL REFERENCES project(id),
    name varchar (255) NOT NULL,
    lat NUMERIC(10,6) NOT NULL,
    lon NUMERIC(10,6) NOT NULL,
--    @TODO consider making city_id reference
    is_local BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_2_group (
    group_id bigserial,
    user_id bigserial
);

CREATE TABLE user_auth (
    user_id bigserial PRIMARY KEY REFERENCES "user"(id),
    name varchar (255),
    password varchar (255)
);

CREATE TABLE city (
    id integer PRIMARY KEY,
    name varchar (255),
    lat NUMERIC(10,6),
    lon NUMERIC(10,6)
);