DELETE FROM comments  ;
ALTER TABLE comments ALTER COLUMN id RESTART WITH 1;

DELETE FROM events  ;
ALTER TABLE events ALTER COLUMN id RESTART WITH 1;

DELETE FROM categories ;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 1;

DELETE FROM users ;
ALTER TABLE users ALTER COLUMN id RESTART WITH 1;