create table if not exists author
(
    id bigint auto_increment primary key,
    name varchar(200) not null UNIQUE
);

create table if not exists book
(
    id         bigint auto_increment primary key,
    title      varchar(100) not null,
    isbn       varchar(50)  not null,
    issue_year smallint     not null,
    author_id  bigint       not null
);

ALTER TABLE book ADD CONSTRAINT book_author_id_fk foreign key (author_id) references author(id) ON DELETE NO ACTION;


create table if not exists genre
(
    id   bigint auto_increment
        constraint genre_pk primary key,
    name varchar(200) not null
);

create unique index if not exists author_name_u1 on author (name);

create table if not exists book_genre
(
    id       bigint auto_increment primary key,
    book_id  integer not null constraint book_id_fk references book on delete no action on delete cascade,
    genre_id integer not null constraint genre_id_fk references genre on delete no action
);

create table if not exists book_comment
(
    id      bigint auto_increment primary key,
    book_id integer      not null  constraint comment_book_id_fk references book on delete no action,
    comment varchar(500) not null
);

