INSERT INTO city (id, name, lat, lon) VALUES
(3078610, 'Brno', 49.195200, 16.608000) ON CONFLICT DO NOTHING;

INSERT INTO city (id, name, lat, lon) VALUES
(3067695, 'Praha', 50.083300, 14.466700) ON CONFLICT DO NOTHING;

INSERT INTO  backer_location (backer_id, city_id) VALUES
(1, 3078610),
(2, 3067695)
;

--@TODO fill with proper id
INSERT INTO address (id, backer_id, street, city, city_id, postal_code) VALUES
(DEFAULT, 1, NULL, NULL, 3078610, NULL),
(DEFAULT, 2, NULL, NULL, 3067695, NULL)
;