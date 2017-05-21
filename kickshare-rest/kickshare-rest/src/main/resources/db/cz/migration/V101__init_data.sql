INSERT INTO project (id, name, description, url, deadline) VALUES
(439380282, 'Quodd Heroes',
    'A completely unique board game with Amazing minis, scenario-based gameplay, and gorgeous art. Action, Adventure and Mayhem await!',
    'https://www.kickstarter.com/projects/wondermentgames/quodd-heroes-0?ref=category', CURRENT_DATE),
(217227567, 'The World of SMOG: Rise of Moloch',
    'A Victorian, adventure board game taking place in an alternative England where magic and technology have taken an extraordinary turn!',
    'https://www.kickstarter.com/projects/coolminiornot/the-world-of-smog-rise-of-moloch?ref=category', CURRENT_DATE),
(1893061183, 'The Edge: Dawnfall', '',
    'https://www.kickstarter.com/projects/awakenrealms/the-edge-dawnfall?ref=category', CURRENT_DATE);

INSERT INTO backer (id, email, name, surname, leader_rating, backer_rating) VALUES
(1, 'xatrix101@gmail.com', 'Jan', 'Kučera', 5, NULL),
(2, 'bruce.wayne@gmail.com', 'Bruce', 'Wayne', 3.1, 4.2),
(3, 'peter.parker@gmail.com', 'Peter', 'Parker', NULL, NULL),
(4, 'tony.stark@gmail.com', 'Tony', 'Stark', NULL, NULL)
;
ALTER SEQUENCE backer_id_seq RESTART WITH 100;

INSERT INTO users (id, username, password, enabled) VALUES
(1, 'xatrix101@gmail.com', '$2a$10$zWVlsjiuimbWVE4D4ZrlseB459kSEeB93e8FM58n8QQBiClwV17Pa', true);

INSERT INTO authorities (username, authority) VALUES
('xatrix101@gmail.com', 'ACTUATOR'),
('xatrix101@gmail.com', 'USER');

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES
(1, 1, 'Quodd Heroes CZ Brno', 439380282, 49.1951, 16.6068, false);
INSERT INTO backer_2_group (group_id, backer_id) VALUES
(1, 1), (1, 2);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES
(2, 1, 'The Edge CZ Brno', 1893061183, 49.1951, 16.6068 );
INSERT INTO backer_2_group (group_id, backer_id) VALUES
(2, 1), (2, 3);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES
(3, 1, 'Rise of Moloch CZ Brno', 217227567, 49.1951, 16.6068 );
INSERT INTO backer_2_group (group_id, backer_id) VALUES (3, 1);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES
(4, 1, 'Rise of Moloch US Gotham', 217227567, 49.1951, 16.6068);
INSERT INTO backer_2_group (group_id, backer_id) VALUES (4, 2);
INSERT INTO backer_2_group (group_id, backer_id) VALUES (4, 3);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES (5, 3, 'Quodd Heroes CZ Praha', 439380282, 50.0833, 14.4666, false );
INSERT INTO backer_2_group (group_id, backer_id) VALUES (5, 2);
INSERT INTO backer_2_group (group_id, backer_id) VALUES (5, 3);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES (6, 2, 'The Edge CZ Praha', 1893061183, 50.0833, 14.4666 );
INSERT INTO backer_2_group (group_id, backer_id) VALUES (6, 1);
INSERT INTO backer_2_group (group_id, backer_id) VALUES (6, 3);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES (7, 4, 'The Edge CZ Ostrava', 1893061183, 49.83332, 18.25, true );
INSERT INTO backer_2_group (group_id, backer_id) VALUES (7, 3);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES (8, 4, 'The Edge CZ České Budějovice', 1893061183, 49.0520, 15.8086, true );
INSERT INTO backer_2_group (group_id, backer_id) VALUES (8, 3);

ALTER SEQUENCE group_id_seq RESTART WITH 100;