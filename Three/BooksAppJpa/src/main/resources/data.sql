INSERT INTO AUTHOR (NAME) VALUES ('Лев Толстой');
INSERT INTO AUTHOR (NAME) VALUES ('Борис Акунин');
INSERT INTO AUTHOR (NAME) VALUES ('Демьян Бедный');

INSERT INTO GENRE (NAME) VALUES ('Детектив');
INSERT INTO GENRE (NAME) VALUES ('Фантастика');
INSERT INTO GENRE (NAME) VALUES ('Исторический роман');
INSERT INTO GENRE (NAME) VALUES ('Детская литература');

INSERT INTO BOOK(TITLE, ISBN, ISSUE_YEAR, AUTHOR_ID) VALUES ('Приключения Фандорина', '12345678', 2010, (SELECT ID FROM AUTHOR WHERE NAME = 'Борис Акунин'));
INSERT INTO BOOK(TITLE, ISBN, ISSUE_YEAR, AUTHOR_ID) VALUES ('История Российского государства', '87654321', 2015, (SELECT ID FROM AUTHOR WHERE NAME = 'Борис Акунин'));
INSERT INTO BOOK(TITLE, ISBN, ISSUE_YEAR, AUTHOR_ID) VALUES ('Война и мир', '123454321', 2001, (SELECT ID FROM AUTHOR WHERE NAME = 'Лев Толстой'));

INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES ((SELECT ID FROM BOOK WHERE ISBN = '12345678'), (SELECT ID FROM GENRE WHERE NAME = 'Детектив'));
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES ((SELECT ID FROM BOOK WHERE ISBN = '12345678'), (SELECT ID FROM GENRE WHERE NAME = 'Фантастика'));
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES ((SELECT ID FROM BOOK WHERE ISBN = '87654321'), (SELECT ID FROM GENRE WHERE NAME = 'Исторический роман'));
INSERT INTO BOOK_GENRE (BOOK_ID, GENRE_ID) VALUES ((SELECT ID FROM BOOK WHERE ISBN = '123454321'), (SELECT ID FROM GENRE WHERE NAME = 'Исторический роман'));

INSERT INTO BOOK_COMMENT (BOOK_ID, COMMENT) VALUES ((SELECT ID FROM BOOK WHERE ISBN = '123454321'), 'Cool book!');
