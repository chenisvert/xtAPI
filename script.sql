create table context
(
    id          int auto_increment
        primary key,
    uid         int          not null,
    name        varchar(100) not null,
    email       varchar(200) not null,
    context     longtext     null,
    address     varchar(100) not null,
    create_time datetime     not null,
    ip          varchar(100) null,
    constraint context_id_uindex
        unique (id)
);

create table onetext
(
    id      int auto_increment
        primary key,
    context int null,
    constraint onetext_id_uindex
        unique (id)
);

create table user
(
    id          int auto_increment
        primary key,
    username    varchar(100) not null,
    password    varchar(100) not null,
    create_time datetime     not null,
    uuid        varchar(100) not null,
    constraint user_id_uindex
        unique (id)
);


