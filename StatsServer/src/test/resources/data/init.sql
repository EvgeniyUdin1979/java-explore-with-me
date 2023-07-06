INSERT INTO public.stats
    (ip, app, created, url)
	VALUES
	('192.168.0.1', 'ewm-main-service', current_timestamp, '/events/1'),
	('192.168.0.2', 'ewm-main-service', current_timestamp, '/events/1'),
	('192.168.0.1', 'ewm-main-service', current_timestamp, '/events/1'),
	('192.168.0.3', 'ewm-main-service', current_timestamp, '/events/1'),
	('192.168.0.1', 'ewm-main-service', current_timestamp, '/events/2'),
	('192.168.0.2', 'ewm-main-service', current_timestamp, '/events/2'),
	('192.168.0.1', 'ewm-main-service', current_timestamp, '/events/2');