INSERT INTO topfive.item(id, user_id, content_type, description, filename, title)
VALUES (25, 1, 'video/mp4', 'Пример видео', 'example_video.mp4', 'Пример видео');
INSERT INTO topfive.item(id, user_id, content_type, description, filename, title)
VALUES (26, 1, 'video/mp4', 'Пример видео2', 'example_video.mp4', 'Пример видео2');
INSERT INTO topfive.item(id, user_id, content_type, description, filename, title)
VALUES (27, 1, 'video/mp4', 'Пример видео3', 'example_video.mp4', 'Пример видео3');
INSERT INTO topfive.item(id, user_id, content_type, description, filename, title)
VALUES (28, 1, 'video/mp4', 'Пример видео4', 'example_video.mp4', 'Пример видео4');

INSERT INTO topfive.video(genre_id, id, released_year, actors, director)
VALUES (9, 25, 2012, 'Вася Петров, Лена Ленина', 'Иван Иванов');
INSERT INTO topfive.video(genre_id, id, released_year, actors, director)
VALUES (9, 26, 2012, null, null);

INSERT INTO topfive.video(genre_id, id, released_year, actors, director)
VALUES (9, 27, 2012, null, null);

INSERT INTO topfive.video(genre_id, id, released_year, actors, director, place)
VALUES (9, 28, 2024, null, null, 'Тайланд');

insert into topfive.top_items(top_id, item_id) values (8, 25);
insert into topfive.top_items(top_id, item_id) values (8, 26);
insert into topfive.top_items(top_id, item_id) values (8, 27);
insert into topfive.top_items(top_id, item_id) values (8, 28);