INSERT INTO "topfive"."item" ("id", "user_id", "content_type", "description", "filename", "title")
VALUES (20, 1, 'audio/mpeg', NULL, 'example_song.mp3', 'Chop Suey!');
INSERT INTO "topfive"."item" ("id", "user_id", "content_type", "description", "filename", "title")
VALUES (21, 1, 'audio/mpeg', NULL, 'example_song.mp3', 'Duality');
INSERT INTO "topfive"."item" ("id", "user_id", "content_type", "description", "filename", "title")
VALUES (22, 1, 'audio/mpeg', NULL, 'example_song.mp3', 'Change(In the House of Flies)');
INSERT INTO "topfive"."item" ("id", "user_id", "content_type", "description", "filename", "title")
VALUES (23, 1, 'audio/mpeg', NULL, 'example_song.mp3', 'Square Hammer');
INSERT INTO "topfive"."item" ("id", "user_id", "content_type", "description", "filename", "title")
VALUES (24, 1, 'audio/mpeg', NULL, 'example_song.mp3', 'Pull Harder on the Strings of Your Martyr');


INSERT INTO "topfive"."song" ("bit_rate", "genre_id", "id", "released_at", "artist")
VALUES (320, 12, 20, '2024-04-24', 'System Of A Down');
INSERT INTO "topfive"."song" ("bit_rate", "genre_id", "id", "released_at", "artist")
VALUES (192, 12, 21, '2020-04-24', 'Slipknot');
INSERT INTO "topfive"."song" ("bit_rate", "genre_id", "id", "released_at", "artist")
VALUES (320, 12, 22, '2013-04-24', 'Deftones');
INSERT INTO "topfive"."song" ("bit_rate", "genre_id", "id", "released_at", "artist")
VALUES (192, 12, 23, '2007-04-24', 'Ghost');
INSERT INTO "topfive"."song" ("bit_rate", "genre_id", "id", "released_at", "artist")
VALUES (128, 12, 24, '2017-04-24', 'Trivium');

INSERT INTO "topfive"."top_items" ("item_id", "top_id") VALUES (20, 2);
INSERT INTO "topfive"."top_items" ("item_id", "top_id") VALUES (21, 2);
INSERT INTO "topfive"."top_items" ("item_id", "top_id") VALUES (22, 2);
INSERT INTO "topfive"."top_items" ("item_id", "top_id") VALUES (23, 2);
INSERT INTO "topfive"."top_items" ("item_id", "top_id") VALUES (24, 2);
