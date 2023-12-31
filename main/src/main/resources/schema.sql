CREATE TABLE IF NOT EXISTS public.users
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    email character varying(255) NOT NULL,
    name character varying(250) NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT email_uk UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS public.categories
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY ,
    name character varying(50) NOT NULL,
    CONSTRAINT categories_pkey PRIMARY KEY (id),
    CONSTRAINT categories_name_uk UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public.events
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    annotation character varying(2000) NOT NULL,
    created timestamp without time zone NOT NULL,
    description character varying(7000) NOT NULL,
    event_date timestamp without time zone NOT NULL,
    lat real NOT NULL,
    lon real NOT NULL,
    paid boolean NOT NULL,
    participant_limit integer NOT NULL,
    published_on timestamp without time zone,
    request_moderation boolean,
    state integer,
    title character varying(120) NOT NULL,
    category_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT events_pkey PRIMARY KEY (id),
    CONSTRAINT events_user_id_fk FOREIGN KEY (user_id)
        REFERENCES public.users (id)
        ON DELETE CASCADE,
    CONSTRAINT events_category_id_fk FOREIGN KEY (category_id)
        REFERENCES public.categories (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.locations
(
    id bigint NOT NULL,
    lat real NOT NULL,
    lon real NOT NULL,
    CONSTRAINT locations_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.requests
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    created timestamp without time zone NOT NULL,
    status character varying(255),
    event_id bigint NOT NULL,
    requester_id bigint NOT NULL,
    CONSTRAINT requests_pkey PRIMARY KEY (id),
    CONSTRAINT requester_event_unique UNIQUE (event_id, requester_id),
    CONSTRAINT requester_id_fk FOREIGN KEY (requester_id)
        REFERENCES public.users (id)
        ON DELETE CASCADE,
    CONSTRAINT requests_event_id_fk FOREIGN KEY (event_id)
        REFERENCES public.events (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.compilations
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    pinned boolean NOT NULL,
    title character varying(50) NOT NULL,
    CONSTRAINT compilations_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.compilations_events
(
    compilation_id bigint NOT NULL,
    event_id bigint NOT NULL,
    CONSTRAINT compilations_events_pkey PRIMARY KEY (compilation_id, event_id),
    CONSTRAINT compilations_events_event_id_fk FOREIGN KEY (event_id)
        REFERENCES public.events (id)
         ON DELETE CASCADE,
    CONSTRAINT compilations_events_compilation_id_fk FOREIGN KEY (compilation_id)
        REFERENCES public.compilations (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public.comments
(
    id bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    text character varying(7000) NOT NULL,
    event_id bigint NOT NULL,
    creator_id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    published_on timestamp without time zone,
    status integer,
    parent_id bigint,
    CONSTRAINT comments_pkey PRIMARY KEY (id),
    CONSTRAINT comments_event_id_fk FOREIGN KEY (event_id)
            REFERENCES public.events (id)
            ON DELETE CASCADE,
    CONSTRAINT comments_creator_id_fk FOREIGN KEY (creator_id)
            REFERENCES public.users (id)
            ON DELETE CASCADE,
    CONSTRAINT comments_parent_id_fk FOREIGN KEY (parent_id)
            REFERENCES public.comments (id)
            ON DELETE NO ACTION
);




