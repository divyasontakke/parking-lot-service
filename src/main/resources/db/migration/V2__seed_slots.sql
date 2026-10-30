INSERT INTO parking_slots (slot_number) SELECT 'A-' || LPAD(i::text, 2, '0') FROM generate_series(1, 10) AS i;
INSERT INTO parking_slots (slot_number) SELECT 'B-' || LPAD(i::text, 2, '0') FROM generate_series(1, 10) AS i;
