insert into topfive.top(id,  title, details, type, user_id)
values (1, 'Top 1', 'Details of 1', 0, 1);

INSERT INTO topfive.item(
    id, user_id, content_type, description, filename, title)
VALUES (2, 1, 'audio/mpeg', 'Cool song', 'Unforgiven.mp3', 'Unforgiven');

INSERT INTO topfive.song(id, artist, released_at, bit_rate, genre_id)
VALUES (2, 'Metallica', '1990-11-29', 192, 1);

insert into topfive.top_items(top_id, item_id) values(1,2);
