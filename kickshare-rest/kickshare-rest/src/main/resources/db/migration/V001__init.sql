--@TODO deal with roles (reader, writer/admin)?
CREATE TABLE city (
    id integer PRIMARY KEY,
    name varchar (255),
    lat NUMERIC(10,6),
    lon NUMERIC(10,6)
);

CREATE TABLE project (
	id BIGSERIAL PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255) NOT NULL,
	url VARCHAR(512) NOT NULL,
	deadline DATE
);

---- @TODO - email must be unique
CREATE TABLE backer (
	id BIGSERIAL PRIMARY KEY,
	email VARCHAR (255),
	name VARCHAR (255) NOT NULL,
	surname VARCHAR (255) NOT NULL,
	leader_rating REAL,
	backer_rating REAL
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    backer_id BIGSERIAL NOT NULL REFERENCES backer(id),
    street VARCHAR (100),
    city VARCHAR (100),
    postal_code VARCHAR (100)
);

CREATE TABLE "group" (
    id BIGSERIAL PRIMARY KEY,
    leader_id BIGINT NOT NULL REFERENCES backer(id),
    project_id BIGINT NOT NULL REFERENCES project(id),
    name VARCHAR(255) NOT NULL,
    lat NUMERIC(10,6) NOT NULL,
    lon NUMERIC(10,6) NOT NULL,
--    @TODO consider making city_id reference
    is_local BOOLEAN DEFAULT TRUE
);

CREATE TABLE project_photo (
    project_id bigserial PRIMARY KEY REFERENCES project (id),
    thumb varchar(512),
	small varchar(512)
--	FOREIGN KEY (project_id) REFERENCES project (id)
--  CONSTRAINT fk_project_photo_project_id foreign key (project_id)
);

CREATE TABLE backer_locations (
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id),
    city_id INTEGER NOT NULL REFERENCES city (id),
    is_permanent_address BOOLEAN DEFAULT FALSE
);

CREATE TABLE backer_2_group (
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id),
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id)
);

CREATE TABLE backer_rating (
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id),
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id),
    rating SMALLINT NOT NULL,
    is_leader BOOLEAN NOT NULL
);

-- @TODO remove once it's moved to Spring table
CREATE TABLE user_auth (
    user_id bigserial PRIMARY KEY REFERENCES backer(id),
    name varchar (255),
    password varchar (255)
);