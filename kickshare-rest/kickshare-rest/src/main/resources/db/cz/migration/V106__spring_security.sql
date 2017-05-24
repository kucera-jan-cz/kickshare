INSERT INTO users (id, username, password, enabled) VALUES
(1, 'xatrix101@gmail.com', '$2a$10$zWVlsjiuimbWVE4D4ZrlseB459kSEeB93e8FM58n8QQBiClwV17Pa', true)
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
(2, 'CREATE_GROUP'),
--admins
(3, 'VIEW_DATA'),
(3, 'CREATE_GROUP'),
(3, 'ACTUATOR')
;

INSERT INTO group_members (id, username, group_id) VALUES
(DEFAULT, 'xatrix101@gmail.com', 3);


