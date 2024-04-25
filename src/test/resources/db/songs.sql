INSERT INTO topfive.item(
    id, user_id, content_type, description, filename, title)
VALUES (2, 1, 'audio/mpeg', 'Cool song', 'Unforgiven.mp3', 'Unforgiven');

INSERT INTO topfive.song(id, artist, released_at, bit_rate, genre_id)
VALUES (2, 'Metallica', '1990-11-29', 192, 12);

insert into topfive.top_items(top_id, item_id) values(1,2);


INSERT INTO topfive.item(
    id, user_id, content_type, description, filename, title)
VALUES (3, 1, 'audio/mpeg', 'Another cool song', 'Fuel.mp3', 'Fuel');

INSERT INTO topfive.song(id, artist, released_at, bit_rate, genre_id)
VALUES (3, 'Metallica', '1996-01-29', 256, 12);

insert into topfive.top_items(top_id, item_id) values(1,3);

alter sequence topfive.common_sequence restart;