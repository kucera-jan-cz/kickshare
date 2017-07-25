CREATE TABLE notification (
    id BIGSERIAL PRIMARY KEY,
    backer_id BIGSERIAL NOT NULL REFERENCES "users" (id),
    sender_id BIGSERIAL NOT NULL REFERENCES "users" (id),
    post_created TIMESTAMP NOT NULL DEFAULT NOW(),
    is_read BOOLEAN NOT NULL DEFAULT true,
    post_text TEXT
);
