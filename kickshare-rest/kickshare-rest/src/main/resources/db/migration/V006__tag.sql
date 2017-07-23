CREATE TABLE tag (
    id SMALLSERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL
);

CREATE TABLE tag_2_group (
    group_id BIGSERIAL NOT NULL REFERENCES "group" (id) ON DELETE CASCADE,
    tag_id SMALLSERIAL NOT NULL REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (group_id, tag_id)
);


CREATE TABLE tag_2_category (
    category_id INTEGER NOT NULL REFERENCES "category" (id) ON DELETE CASCADE,
    tag_id SMALLSERIAL NOT NULL REFERENCES tag (id) ON DELETE CASCADE,
    PRIMARY KEY (category_id, tag_id)
);