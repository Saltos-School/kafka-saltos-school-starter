CREATE STREAM compras (id INTEGER KEY, producto_id INTEGER, cliente_id INTEGER, total DECIMAL(12, 2)) \
  WITH (KAFKA_TOPIC='compras', PARTITIONS = 2, VALUE_FORMAT='JSON');


INSERT INTO compras (id, producto_id, cliente_id, total) VALUES (0, 1089, 5, 42.20);
INSERT INTO compras (id, producto_id, cliente_id, total) VALUES (1, 725, 7, 415.25);
INSERT INTO compras (id, producto_id, cliente_id, total) VALUES (2, 8909, 4, 250.70);

SELECT * FROM compras WHERE total < 300;

SELECT * FROM compras WHERE total < 300 EMIT CHANGES;

CREATE STREAM compras_caras AS SELECT * FROM compras WHERE total >= 300;

INSERT INTO compras (*) VALUES (3, 725, 6, 415.25);

CREATE TABLE compras_total AS
  SELECT id, SUM(total) AS total_compra FROM compras
  WINDOW TUMBLING (SIZE 30 MINUTES)
  GROUP BY id
  EMIT CHANGES;