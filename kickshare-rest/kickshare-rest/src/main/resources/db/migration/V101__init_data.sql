SET SCHEMA KICKSHARE;

INSERT INTO project (id, name, description, url, deadline) VALUES (
    439380282, 'Quodd Heroes',
    'A completely unique board game with Amazing minis, scenario-based gameplay, and gorgeous art. Action, Adventure and Mayhem await!',
    'https://www.kickstarter.com/projects/wondermentgames/quodd-heroes-0?ref=category',
    DATEADD('MONTH', 1, CURRENT_DATE)
);

INSERT INTO user (id, email, name, surname) VALUES (1, 'xatrix101@gmail.com', 'Jan', 'Kučera');
INSERT INTO user (id, email, name, surname) VALUES (2, 'bruce.wayne@gmail.com', 'Bruce', 'Wayne');
INSERT INTO user (id, email, name, surname) VALUES (3, 'peter.parker@gmail.com', 'Peter', 'Parker');

INSERT INTO `group` (id, leader_id, name) VALUES (1, 1, 'Quodd Heroes CZ');
INSERT INTO user_2_group (group_id, user_id) VALUES (1, 1);
INSERT INTO user_2_group (group_id, user_id) VALUES (1, 2);

INSERT INTO `group` (id, leader_id, name) VALUES (2, 1, 'The Edge CZ');
INSERT INTO user_2_group (group_id, user_id) VALUES (2, 1);
INSERT INTO user_2_group (group_id, user_id) VALUES (2, 3);