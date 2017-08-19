CREATE TABLE category (
    id INTEGER PRIMARY KEY,
    name VARCHAR (255) NOT NULL,
    is_root BOOLEAN NOT NULL,
    parent_id INTEGER,
    slug VARCHAR (255)
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

CREATE TABLE city (
    id INTEGER PRIMARY KEY,
    name varchar (255),
    lat NUMERIC(10,6),
    lon NUMERIC(10,6)
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    backer_id BIGSERIAL NOT NULL REFERENCES backer(id),
    street VARCHAR (100),
    city VARCHAR (100),
    city_id INTEGER REFERENCES city(id),
    postal_code VARCHAR (16)
);

CREATE TABLE project_photo (
    project_id bigserial PRIMARY KEY REFERENCES project (id) ON DELETE CASCADE,
    thumb varchar(512),
	small varchar(512),
	little VARCHAR(512),
    ed VARCHAR(512),
    med VARCHAR(512),
    "full" VARCHAR(512)
);

-- @TODO - consider usage of address or backer_location
CREATE TABLE backer_location (
    backer_id BIGSERIAL PRIMARY KEY REFERENCES backer (id),
    city_id INTEGER NOT NULL REFERENCES city (id)
);

CREATE TABLE backer_setting (
    backer_id BIGSERIAL PRIMARY KEY REFERENCES backer (id),
    category_id INTEGER NOT NULL REFERENCES category (id)
);

-- @TODO remove once it's moved to Spring table
CREATE TABLE user_auth (
    user_id BIGSERIAL PRIMARY KEY REFERENCES backer(id),
    name VARCHAR (255),
    password VARCHAR (255)
);

CREATE TABLE private_msg (
  msg_id BIGSERIAL PRIMARY KEY,
  subject VARCHAR(255) NOT NULL DEFAULT '',
  from_user_id BIGSERIAL NOT NULL,
  to_user_id BIGSERIAL NOT NULL,
  msg_created TIMESTAMP DEFAULT NOW(),
  msg_text TEXT
);