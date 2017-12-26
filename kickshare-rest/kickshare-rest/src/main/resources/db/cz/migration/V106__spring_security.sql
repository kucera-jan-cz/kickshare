INSERT INTO users (id, username, password, enabled, token) VALUES
--test user# pwd: user
(1, 'xatrix101@gmail.com', '$2a$10$zWVlsjiuimbWVE4D4ZrlseB459kSEeB93e8FM58n8QQBiClwV17Pa', true, 'deadbeef-dead-beef-dead-beef00000057'),
--test user# pwd: user
(2, 'bruce.wayne@gmail.com', '$2a$10$irs87L9Y5ZSuzy4aK1ibbeXOYXYLMtGHwfWLQoQI.T.ueVX./YE7i', true, 'deadbeef-dead-beef-dead-beef00000058'),
(3, 'peter.parker@gmail.com', '$2a$10$RpLKHiqgdMZUggGIYJnV6u8sS8hbSNBCzrN4axbBPdEviWSX5JNo6', true, 'deadbeef-dead-beef-dead-beef00000058'),
(4, 'tony.stark@gmail.com', '$2a$10$OR0MbXFHYKkPqi.HKGeadusEgbYlGxshvKCxhGjmnAxDiy6sN8aYK', true, 'deadbeef-dead-beef-dead-beef00000058')
;

INSERT INTO authorities (username, authority) VALUES
('xatrix101@gmail.com', 'ACTUATOR'),
('xatrix101@gmail.com', 'USER')
;

INSERT INTO groups (id, group_name) VALUES
(1, 'backers'),
(2, 'leaders'),
(3, 'admins')
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
(DEFAULT, 'bruce.wayne@gmail.com', 2),
(DEFAULT, 'peter.parker@gmail.com', 2),
(DEFAULT, 'tony.stark@gmail.com', 2)
;


