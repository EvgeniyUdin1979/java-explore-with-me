DELETE FROM stats;
ALTER TABLE stats ALTER COLUMN id RESTART WITH 1;