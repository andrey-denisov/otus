insert into author (name) values ('Борис Акунин');
insert into author (name) values ('Лев Толстой');

insert into genre (name) values ('Детектив');
insert into genre (name) values ('Фантастика');
insert into genre (name) values ('Исторический роман');
insert into genre (name) values ('Детская литература');

insert into book(title, isbn, issue_year, author_id) values ('Приключения Фандорина', '12345678', 2010, (SELECT id from Author where name = 'Борис Акунин'));
insert into book(title, isbn, issue_year, author_id) values ('История Российского государства', '87654321', 2015, (SELECT id from Author where name = 'Борис Акунин'));
insert into book(title, isbn, issue_year, author_id) values ('Война и мир', '123454321', 2001, (SELECT id from Author where name = 'Лев Толстой'));

insert into book_genre (book_id, genre_id) values ((SELECT id from Book where isbn = '12345678'), (SELECT id from genre where name = 'Детектив'));
insert into book_genre (book_id, genre_id) values ((SELECT id from Book where isbn = '12345678'), (SELECT id from genre where name = 'Фантастика'));
insert into book_genre (book_id, genre_id) values ((SELECT id from Book where isbn = '87654321'), (SELECT id from genre where name = 'Исторический роман'));
insert into book_genre (book_id, genre_id) values ((SELECT id from Book where isbn = '123454321'), (SELECT id from genre where name = 'Исторический роман'));

