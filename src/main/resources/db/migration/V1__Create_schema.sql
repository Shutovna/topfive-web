SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;


create schema if not exists topfive;


--
-- Name: common_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE topfive.common_sequence
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE topfive.users (
                               id integer NOT NULL,
                               username character varying(255) UNIQUE NOT NULL,
                               password character varying(255) NOT NULL
);

CREATE TABLE topfive.role (
                               id integer NOT NULL,
                               name character varying(255) UNIQUE NOT NULL
);

CREATE TABLE topfive.user_roles (
                               user_id integer NOT NULL,
                               role_id integer NOT NULL
);

--
-- Name: genre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.genre (
                              id integer NOT NULL,
                              name character varying(255) NOT NULL
);


--
-- Name: item; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.item (
                             id integer NOT NULL,
                             user_id integer NOT NULL,
                             content_type character varying(255) NOT NULL,
                             description character varying(255),
                             filename character varying(255) NOT NULL,
                             title character varying(255) NOT NULL
);


--
-- Name: item_ratings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.item_ratings (
                                     item_id integer NOT NULL,
                                     ratings_id integer NOT NULL
);


--
-- Name: photo; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.photo (
                              id integer NOT NULL,
                              model_name character varying(255)
);


--
-- Name: rating; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.rating (
                               id integer NOT NULL,
                               rating_value integer,
                               user_id integer NOT NULL
);

--
-- Name: song; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.song (
                             bit_rate integer,
                             genre_id integer,
                             id integer NOT NULL,
                             released_at date,
                             artist character varying(255) NOT NULL
);


--
-- Name: top; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.top (
                            id integer NOT NULL,
                            type smallint NOT NULL,
                            user_id integer NOT NULL,
                            details character varying(255),
                            title character varying(255) NOT NULL,
                            CONSTRAINT top_type_check CHECK (((type >= 0) AND (type <= 2)))
);


--
-- Name: top_items; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.top_items (
                                  item_id integer NOT NULL,
                                  top_id integer NOT NULL
);

--
-- Name: top_ratings; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.top_ratings (
                                    ratings_id integer NOT NULL,
                                    top_id integer NOT NULL
);




--
-- Name: video; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE topfive.video (
                              genre_id integer,
                              id integer NOT NULL,
                              released_year integer,
                              actors character varying(255),
                              director character varying(255)
);


ALTER TABLE ONLY topfive.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);

ALTER TABLE ONLY topfive.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY topfive.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: genre genre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.genre
    ADD CONSTRAINT genre_pkey PRIMARY KEY (id);


--
-- Name: item item_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.item
    ADD CONSTRAINT item_pkey PRIMARY KEY (id);


--
-- Name: item_ratings item_ratings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.item_ratings
    ADD CONSTRAINT item_ratings_pkey PRIMARY KEY (item_id, ratings_id);


--
-- Name: item_ratings item_ratings_ratings_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.item_ratings
    ADD CONSTRAINT item_ratings_ratings_id_key UNIQUE (ratings_id);


--
-- Name: photo photo_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.photo
    ADD CONSTRAINT photo_pkey PRIMARY KEY (id);


--
-- Name: rating rating_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.rating
    ADD CONSTRAINT rating_pkey PRIMARY KEY (id);


--
-- Name: song song_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.song
    ADD CONSTRAINT song_pkey PRIMARY KEY (id);


--
-- Name: top_items top_items_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.top_items
    ADD CONSTRAINT top_items_pkey PRIMARY KEY (item_id, top_id);


--
-- Name: top top_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.top
    ADD CONSTRAINT top_pkey PRIMARY KEY (id);


--
-- Name: top_ratings top_ratings_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.top_ratings
    ADD CONSTRAINT top_ratings_pkey PRIMARY KEY (ratings_id, top_id);


--
-- Name: video video_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.video
    ADD CONSTRAINT video_pkey PRIMARY KEY (id);


ALTER TABLE ONLY topfive.top
    ADD CONSTRAINT top_user_fkey FOREIGN KEY (user_id) REFERENCES topfive.users(id);

ALTER TABLE ONLY topfive.item
    ADD CONSTRAINT item_user_fkey FOREIGN KEY (user_id) REFERENCES topfive.users(id);

ALTER TABLE ONLY topfive.rating
    ADD CONSTRAINT rating_user_fkey FOREIGN KEY (user_id) REFERENCES topfive.users(id);

ALTER TABLE ONLY topfive.user_roles
    ADD CONSTRAINT user_roles_user_fkey FOREIGN KEY (user_id) REFERENCES topfive.users(id);

ALTER TABLE ONLY topfive.user_roles
    ADD CONSTRAINT user_roles_role_fkey FOREIGN KEY (role_id) REFERENCES topfive.role(id);

--
-- Name: top_items fk1padwwjbr7l4wke6jopb5dlfv; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.top_items
    ADD CONSTRAINT fk1padwwjbr7l4wke6jopb5dlfv FOREIGN KEY (item_id) REFERENCES topfive.item(id);


--
-- Name: top_ratings fk21kj36xm5qfj0oxwm7n7dfd4n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.top_ratings
    ADD CONSTRAINT fk21kj36xm5qfj0oxwm7n7dfd4n FOREIGN KEY (ratings_id) REFERENCES topfive.rating(id);


--
-- Name: song fk3kr980xhy18ojchq1ekbevypy; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.song
    ADD CONSTRAINT fk3kr980xhy18ojchq1ekbevypy FOREIGN KEY (genre_id) REFERENCES topfive.genre(id);

--
-- Name: video fk3nfhh71ksu8q2i4q7gojl14q9; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.video
    ADD CONSTRAINT fk3nfhh71ksu8q2i4q7gojl14q9 FOREIGN KEY (id) REFERENCES topfive.item(id);


--
-- Name: top_ratings fk4c4tbunjf46d41e65iftets69; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.top_ratings
    ADD CONSTRAINT fk4c4tbunjf46d41e65iftets69 FOREIGN KEY (top_id) REFERENCES topfive.top(id);


--
-- Name: photo fk4ih6b8xjvtv0m02dh4eb6n1pc; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.photo
    ADD CONSTRAINT fk4ih6b8xjvtv0m02dh4eb6n1pc FOREIGN KEY (id) REFERENCES topfive.item(id);


--
-- Name: song fk86gvq3d1mbu7blyykyp96hffn; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.song
    ADD CONSTRAINT fk86gvq3d1mbu7blyykyp96hffn FOREIGN KEY (id) REFERENCES topfive.item(id);


--
-- Name: video fkby69dcb5gt1coe0o6ucovnj9t; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.video
    ADD CONSTRAINT fkby69dcb5gt1coe0o6ucovnj9t FOREIGN KEY (genre_id) REFERENCES topfive.genre(id);


--
-- Name: item fkc4s174l330le17rblwgyjawev; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

--
-- Name: rating fkf68lgbsbxl310n0jifwpfqgfh; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

--
-- Name: top_items fkg5y8mrhxy5glt3jv0e9ru0x9l; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.top_items
    ADD CONSTRAINT fkg5y8mrhxy5glt3jv0e9ru0x9l FOREIGN KEY (top_id) REFERENCES topfive.top(id);


--
-- Name: item_ratings fko151598m37jr38qchqcobu55k; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.item_ratings
    ADD CONSTRAINT fko151598m37jr38qchqcobu55k FOREIGN KEY (item_id) REFERENCES topfive.item(id);


--
-- Name: item_ratings fkoirddo9qaeenv6pjg474f9ar6; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY topfive.item_ratings
    ADD CONSTRAINT fkoirddo9qaeenv6pjg474f9ar6 FOREIGN KEY (ratings_id) REFERENCES topfive.rating(id);