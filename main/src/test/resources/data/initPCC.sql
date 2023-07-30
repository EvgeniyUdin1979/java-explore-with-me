insert into users (email, name) values ('user1@yandex.ru', 'Вася Пупкин');

insert into categories (name) values ('Концерты');

INSERT INTO public.events(
	 annotation, created, description, event_date, lat, lon, paid, participant_limit, published_on, request_moderation, state, title, category_id, user_id)
	VALUES ( 'annotation', current_timestamp, 'description', current_timestamp + time '02:00:00', 0, 0, 'true', 1, null, 'false', 1, 'title', 1, 1);