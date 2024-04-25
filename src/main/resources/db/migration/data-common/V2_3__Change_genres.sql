alter table topfive.genre
    add parent_id integer;

ALTER TABLE ONLY topfive.genre
    ADD CONSTRAINT genre_parent_fk FOREIGN KEY (parent_id) REFERENCES topfive.genre (id);

delete from topfive.genre;

insert into topfive.genre(id, name, parent_id)
values (1, 'Жанры музыки', null),
       (2, 'Жанры видео', null),
       (10, 'Поп', 1),
       (11, 'Электронная', 1),
       (12, 'Рок', 1),
       (13, 'Классика', 1),
       (14, 'Комедия', 2),
       (15, 'Боевик', 2),
       (16, 'Фантастика', 2),
       (17, 'Любительское', 2);



