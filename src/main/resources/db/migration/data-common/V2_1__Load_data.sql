insert into topfive.genre (id, name) values (1, 'Metall');
insert into topfive.genre (id, name) values (2, 'Pop');
insert into topfive.genre (id, name) values (3, 'Trance');
insert into topfive.genre (id, name) values (4, 'Classic');

insert into topfive.users(id, username, password)
values (1, 'shutovna','$2a$12$61mFvUnwpU5PSSmg13Mu/.IKmrP9CQbTAu2H7sMNUnKXVB05PIHD6');

insert into topfive.role(id, name) values(1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

insert into topfive.user_roles(user_id, role_id) values (1, 1), (1,2);