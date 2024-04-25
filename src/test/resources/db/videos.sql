INSERT INTO topfive.item(
    id, user_id, content_type, description, filename, title)
VALUES (5, 1, 'video/mpeg', 'Cool video', 'Video.mp4', 'Video');

INSERT INTO topfive.video(
    genre_id, id, released_year, actors, director, place)
VALUES (15, 5, 2000, 'Actors list 1', 'Director 1', 'Place 1');

INSERT INTO topfive.item(
    id, user_id, content_type, description, filename, title)
VALUES (6, 1, 'video/mpeg', 'Cool video2', 'Video2.mp4', 'Video2');

INSERT INTO topfive.video(
    genre_id, id, released_year, actors, director, place)
VALUES (16, 6, 2005, 'Actors list 2', 'Director 2', 'Place 2');


alter sequence topfive.common_sequence restart;