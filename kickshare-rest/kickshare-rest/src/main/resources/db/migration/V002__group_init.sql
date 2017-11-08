--@TODO figure out whether this is MVP
CREATE TABLE group_status (
    id INTEGER PRIMARY KEY,
    "name" VARCHAR(64)
);

INSERT INTO group_status (id, "name") VALUES
(100, 'OPEN'),
--(150, 'LOCKED'),
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
    is_local BOOLEAN DEFAULT TRUE,
    "limit" INTEGER DEFAULT 10
);

CREATE TYPE group_request_status AS ENUM ('REQUESTED','APPROVED', 'DECLINED');

CREATE TABLE backer_2_group (
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id) ON DELETE CASCADE,
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id),
    status group_request_status DEFAULT 'REQUESTED',
    request_msg TEXT,
    PRIMARY KEY (group_id, backer_id)
);

CREATE TABLE backer_2_group_delivery (
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id) ON DELETE CASCADE,
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id),
    pick_up_city_id INTEGER NOT NULL REFERENCES city (id),
    personal_takeover BOOLEAN NOT NULL,
    delivered BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (group_id, backer_id)
);

CREATE TABLE group_post (
  post_id BIGSERIAL,
  group_id BIGSERIAL NOT NULL REFERENCES "group" (id) ON DELETE CASCADE,
  backer_id BIGSERIAL NOT NULL,
  post_created TIMESTAMP NOT NULL DEFAULT NOW(),
  post_modified TIMESTAMP NOT NULL DEFAULT NOW(),
  post_edit_count INTEGER NOT NULL DEFAULT 0,
  post_text TEXT,
  PRIMARY KEY (post_id)
);

CREATE TABLE backer_rating (
    backer_id BIGSERIAL NOT NULL REFERENCES backer (id),
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id),
    rating SMALLINT NOT NULL,
    is_leader BOOLEAN NOT NULL
);
