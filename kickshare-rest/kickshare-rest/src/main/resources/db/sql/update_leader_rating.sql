UPDATE backer b
SET leader_rating = lr.rating
FROM (
       SELECT r.leader_id, AVG(r.rating) AS rating
       FROM leader_rating AS r
       GROUP BY r.leader_id
     ) AS lr
WHERE b.id = lr.leader_id