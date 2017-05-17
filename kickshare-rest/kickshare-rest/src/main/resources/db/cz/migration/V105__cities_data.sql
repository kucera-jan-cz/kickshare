INSERT INTO city (id, name, lat, lon) VALUES
(3078610, 'Brno', 49.195200, 16.608000) ON CONFLICT DO NOTHING;

INSERT INTO city (id, name, lat, lon) VALUES
(3067695, '3067695', 50.083300, 14.466700) ON CONFLICT DO NOTHING;

INSERT INTO  backer_locations (backer_id, city_id, is_permanent_address) VALUES
(1, 3078610, TRUE),
(2, 3067695, TRUE);
