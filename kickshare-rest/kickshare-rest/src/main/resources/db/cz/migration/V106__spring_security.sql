INSERT INTO users (id, username, password, enabled, token) VALUES
(1, 'xatrix101@gmail.com', '$2a$10$zWVlsjiuimbWVE4D4ZrlseB459kSEeB93e8FM58n8QQBiClwV17Pa', true, 'deadbeef-dead-beef-dead-beef00000057'),
(2, 'bruce.wayne@gmail.com', '$2a$10$dG0.tCsriLNRpsJZvoDRauH7zrvEA1QT9SUAKZeP5PN9O.w5bkzE6', true, 'deadbeef-dead-beef-dead-beef00000058')
;

INSERT INTO authorities (username, authority) VALUES
('xatrix101@gmail.com', 'ACTUATOR'),
('xatrix101@gmail.com', 'USER')
;

INSERT INTO groups (group_name) VALUES
('backers'),
('leaders'),
('admins')
;

INSERT INTO group_authorities (group_id, authority) VALUES
--backers
(1, 'VIEW_DATA'),
--leaders
(2, 'VIEW_DATA'),
(2, 'CREATE_GROUP'),
--admins
(3, 'VIEW_DATA'),
(3, 'CREATE_GROUP'),
(3, 'ACTUATOR'),
(3, 'ADMIN')
;

INSERT INTO group_members (id, username, group_id) VALUES
(DEFAULT, 'xatrix101@gmail.com', 3),
(DEFAULT, 'bruce.wayne@gmail.com', 2)
;


