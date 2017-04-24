--@TODO deal with roles (reader, writer/admin)?
CREATE TABLE category (
    id INTEGER PRIMARY KEY,
    name VARCHAR (255) NOT NULL,
    is_root BOOLEAN NOT NULL,
    parent_id INTEGER,
    slug VARCHAR (255)
);


