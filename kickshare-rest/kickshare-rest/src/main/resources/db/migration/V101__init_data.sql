INSERT INTO project (id, name, description, url, deadline) VALUES (
    439380282, 'Quodd Heroes',
    'A completely unique board game with Amazing minis, scenario-based gameplay, and gorgeous art. Action, Adventure and Mayhem await!',
    'https://www.kickstarter.com/projects/wondermentgames/quodd-heroes-0?ref=category',
    DATEADD('MONTH', 1, CURRENT_DATE)
);

INSERT INTO project (id, name, description, url, deadline) VALUES (
    217227567, 'The World of SMOG: Rise of Moloch',
    'A Victorian, adventure board game taking place in an alternative England where magic and technology have taken an extraordinary turn!',
    'https://www.kickstarter.com/projects/coolminiornot/the-world-of-smog-rise-of-moloch?ref=category',
    DATEADD('MONTH', 1, CURRENT_DATE)
);

INSERT INTO project (id, name, description, url, deadline) VALUES (
    1893061183, 'The Edge: Dawnfall',
    '',
    'https://www.kickstarter.com/projects/awakenrealms/the-edge-dawnfall?ref=category',
    DATEADD('MONTH', 1, CURRENT_DATE)
);

INSERT INTO user (id, email, name, surname) VALUES (1, 'xatrix101@gmail.com', 'Jan', 'Kučera');
INSERT INTO user (id, email, name, surname) VALUES (2, 'bruce.wayne@gmail.com', 'Bruce', 'Wayne');
INSERT INTO user (id, email, name, surname) VALUES (3, 'peter.parker@gmail.com', 'Peter', 'Parker');

INSERT INTO `group` (id, leader_id, name, project_id) VALUES (1, 1, 'Quodd Heroes CZ', 439380282);
INSERT INTO user_2_group (group_id, user_id) VALUES (1, 1);
INSERT INTO user_2_group (group_id, user_id) VALUES (1, 2);

INSERT INTO `group` (id, leader_id, name, project_id) VALUES (2, 1, 'The Edge CZ', 1893061183);
INSERT INTO user_2_group (group_id, user_id) VALUES (2, 1);
INSERT INTO user_2_group (group_id, user_id) VALUES (2, 3);

INSERT INTO `group` (id, leader_id, name, project_id) VALUES (3, 1, 'Rise of Moloch CZ Brno', 217227567);
INSERT INTO user_2_group (group_id, user_id) VALUES (3, 1);

INSERT INTO `group` (id, leader_id, name, project_id) VALUES (4, 1, 'Rise of Moloch US Gotham', 217227567);
INSERT INTO user_2_group (group_id, user_id) VALUES (4, 2);
INSERT INTO user_2_group (group_id, user_id) VALUES (4, 3);