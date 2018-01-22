UPDATE backer b
SET backer_rating = br.rating
FROM (
       SELECT r.backer_id, AVG(r.rating) AS rating
       FROM backer_rating AS r
       GROUP BY r.backer_id
     ) AS br
WHERE b.id = br.backer_id