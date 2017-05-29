--@TODO deal with roles (reader, writer/admin)?
CREATE TABLE city (
    id INTEGER PRIMARY KEY,
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

CREATE TABLE backer (
	id BIGSERIAL PRIMARY KEY,
	email VARCHAR (255) UNIQUE,
	name VARCHAR (255) NOT NULL,
	surname VARCHAR (255) NOT NULL,
	leader_rating REAL,
	backer_rating REAL
);

CREATE TABLE leader (
    backer_id BIGSERIAL PRIMARY KEY REFERENCES backer(id),
    email VARCHAR (255) UNIQUE,
    kickstarter_id BIGINT UNIQUE
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    backer_id BIGSERIAL NOT NULL REFERENCES backer(id),
    street VARCHAR (100),
    city VARCHAR (100),
    postal_code VARCHAR (100)
);

--@TODO figure out whether this is MVP
CREATE TABLE group_status (
    id INTEGER PRIMARY KEY,
    "name" VARCHAR(64)
);

INSERT INTO group_status (id, name) VALUES
(100, 'OPEN'),
(200, 'BACKED'),
(300, 'BACKERS_PAYING'),
(400, 'KICKSTARTER_WAITING'),
(500, 'DELIVERING'),
(600, 'COMPLETED')
;

CREATE TABLE "group" (
    id BIGSERIAL PRIMARY KEY,
    leader_id BIGINT NOT NULL REFERENCES backer(id),
    project_id BIGINT NOT NULL REFERENCES project(id),
    name VARCHAR(255) NOT NULL,
--    @TODO bind with city
    city_id INTEGER,
    lat NUMERIC(10,6) NOT NULL,
    lon NUMERIC(10,6) NOT NULL,
    is_local BOOLEAN DEFAULT TRUE
);

CREATE TABLE project_photo (
    project_id bigserial PRIMARY KEY REFERENCES project (id) ON DELETE CASCADE,
    thumb varchar(512),
	small varchar(512)
--	FOREIGN KEY (project_id) REFERENCES project (id)
--  CONSTRAINT fk_project_photo_project_id foreign key (project_id)
);

CREATE TABLE backer_locations (
    backer_id BIGSERIAL PRIMARY KEY REFERENCES backer (id),
    city_id INTEGER NOT NULL REFERENCES city (id),
    is_permanent_address BOOLEAN DEFAULT FALSE
);

CREATE TYPE group_request_status AS ENUM ('REQUESTED','APPROVED', 'DECLINED');

CREATE TABLE backer_2_group (
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id) ON DELETE CASCADE,
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id),
    status group_request_status DEFAULT 'REQUESTED',
    PRIMARY KEY (group_id, backer_id)
);

CREATE TABLE backer_rating (
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id),
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id),
    rating SMALLINT NOT NULL,
    is_leader BOOLEAN NOT NULL
);

-- @TODO remove once it's moved to Spring table
CREATE TABLE user_auth (
    user_id BIGSERIAL PRIMARY KEY REFERENCES backer(id),
    name VARCHAR (255),
    password VARCHAR (255)
);

CREATE TABLE category (
    id INTEGER PRIMARY KEY,
    name VARCHAR (255) NOT NULL,
    is_root BOOLEAN NOT NULL,
    parent_id INTEGER,
    slug VARCHAR (255)
);