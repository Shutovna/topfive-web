INSERT INTO topfive.item(
    id, user_id, content_type, description, filename, title)
VALUES (5, 1, 'video/mpeg', 'Cool video', 'Video.mp4', 'Video');

INSERT INTO topfive.video(id)
VALUES (5);

INSERT INTO topfive.item(
    id, user_id, content_type, description, filename, title)
VALUES (6, 1, 'video/mpeg', 'Cool video2', 'Video2.mp4', 'Video2');

INSERT INTO topfive.video(id)
VALUES (6);


alter sequence topfive.common_sequence restart;