INSERT INTO project (id, category_id, name, description, url, deadline) VALUES
(439380282, 34, 'Quodd Heroes', 'A completely unique board game with Amazing minis, scenario-based gameplay, and gorgeous art. Action, Adventure and Mayhem await!', 'https://www.kickstarter.com/projects/wondermentgames/quodd-heroes-0?ref=category', '2017-03-03'),
(933266637, 34, 'Quodd Heroes - A fast, fun, tumble-powered board game (Canceled)', 'Awesome miniatures, truly unique mechanics, scenario-based gameplay, and gorgeous art.  Action, adventure and Mayhem await!', 'https://www.kickstarter.com/projects/wondermentgames/quodd-heroes?ref=category', '2017-01-03'),
(217227567, 34, 'The World of SMOG: Rise of Moloch', 'A Victorian, adventure board game taking place in an alternative England where magic and technology have taken an extraordinary turn!', 'https://www.kickstarter.com/projects/cmon/the-world-of-smog-rise-of-moloch?ref=category', '2017-02-03'),
(1893061183, 34, 'The Edge: Dawnfall', 'Next level competitive miniature Board Game for 1-4 players from creator of Neuroshima Hex and Cry Havoc.', 'https://www.kickstarter.com/projects/awakenrealms/the-edge-dawnfall?ref=category', '2016-11-14')
;

INSERT INTO backer (id, email, name, surname, leader_rating, backer_rating) VALUES
(1, 'xatrix101@gmail.com', 'Jan', 'Kučera', 5, NULL),
(2, 'bruce.wayne@gmail.com', 'Bruce', 'Wayne', 3.1, 4.2),
(3, 'peter.parker@gmail.com', 'Peter', 'Parker', NULL, NULL),
(4, 'tony.stark@gmail.com', 'Tony', 'Stark', NULL, NULL)
;
ALTER SEQUENCE backer_id_seq RESTART WITH 100;

--@TODO consider delete in favor of generic backer_settings
INSERT INTO backer_setting (backer_id, category_id) VALUES
(1, 34),
(2, 34),
(3, 12),
(4, 12)
;

INSERT INTO leader (backer_id, email, kickstarter_id) VALUES
(1, 'kickshare.eu@gmail.com', 1564258620);

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES
(1, 1, 'Quodd Heroes - CZ', 439380282, 49.1951, 16.6068, false);
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES
(1, 1, 'APPROVED'), (1, 2, 'APPROVED');
INSERT INTO backer_rating (author_id,  group_id, backer_id, rating, "message") VALUES
(2, 1, 1, 5.0, 'Excellent delivery');

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES
(2, 1, 'The Edge - Brno', 1893061183, 49.1951, 16.6068 );
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES
(2, 1, 'APPROVED'), (2, 3, 'APPROVED');

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES
(3, 1, 'Rise of Moloch - Brno', 217227567, 49.1951, 16.6068 );
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES
(3, 1, 'APPROVED'),  (3, 2, 'APPROVED'),  (3, 3, 'APPROVED');

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES
(4, 2, 'Rise of Moloch - Gotham', 217227567, 49.1951, 16.6068);
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES
(4, 2, 'APPROVED'), (4, 3, 'APPROVED');

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES
(5, 3, 'Quodd Heroes - Praha', 439380282, 50.0833, 14.4666, false );
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES
(5, 2, 'APPROVED'), (5, 3, 'APPROVED'), (5, 1, 'APPROVED');
INSERT INTO backer_rating (author_id,  group_id, backer_id, rating, "message") VALUES
(3, 5, 1, 4.0, 'Thinks Batman is the best...');
INSERT INTO leader_rating (author_id, group_id, leader_id, rating, "message") VALUES
(2, 5, 3, 4.0, 'Funny guy but is from Marvel'),
(1, 5, 3, 5.0, 'Always friendly neighbour')
;

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon) VALUES
(6, 2, 'The Edge - Praha', 1893061183, 50.0833, 14.4666 );
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES
(6, 1, 'APPROVED'), (6, 2, 'APPROVED');

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES
(7, 4, 'The Edge - CZ', 1893061183, 49.83332, 18.25, true );
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES (7, 4, 'APPROVED');

INSERT INTO "group" (id, leader_id, name, project_id, lat, lon, is_local) VALUES
(8, 4, 'The Edge - CZ #02', 1893061183, 49.0520, 15.8086, true );
INSERT INTO backer_2_group (group_id, backer_id, status) VALUES (8, 4, 'APPROVED');

ALTER SEQUENCE group_id_seq RESTART WITH 100;

INSERT INTO "group_post" ("group_id", "backer_id", "post_created", "post_edit_count", "post_text") VALUES
(1, 2, timestamp '2018-01-01 14:09:17.911', 0, 'Minima explicabo officia expedita sed nihil iure.'),
(1, 2, timestamp '2018-01-05 14:09:17.911', 0, 'Voluptatem molestiae ab qui molestiae repudiandae.'),
(1, 1, timestamp '2018-01-17 14:09:17.911', 0, 'Voluptas necessitatibus modi earum molestiae autem.'),
(1, 1, timestamp '2018-01-17 14:09:17.911', 0, 'Distinctio molestiae qui deleniti dicta.'),
(1, 2, timestamp '2018-01-17 14:09:17.911', 0, 'Quidem explicabo animi enim.'),
(1, 2, timestamp '2018-01-20 14:09:17.911', 0, 'Ut at explicabo numquam molestias molestiae officiis eligendi.'),
(1, 1, timestamp '2018-01-25 14:09:17.911', 0, 'Sit impedit quibusdam.'),
(1, 2, timestamp '2018-01-26 14:09:17.911', 0, 'Ullam est ut odit rerum illum commodi blanditiis.'),
(1, 1, timestamp '2018-01-27 13:09:17.911', 0, 'Eveniet quia eveniet porro laborum eius itaque esse.'),
(1, 2, timestamp '2018-01-28 14:09:17.911', 0, 'Deleniti cupiditate qui eius.'),
(1, 1, timestamp '2018-01-28 17:09:17.911', 0, 'Voluptas explicabo animi enim.'),
(1, 1, timestamp '2018-01-28 19:11:17.911', 0, 'Eveniet impedit quibusdam.')
;