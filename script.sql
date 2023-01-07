create table api
(
    id   int auto_increment
        primary key,
    name varchar(100)  not null comment 'api的名字',
    size int default 0 null comment '总调用次数',
    constraint api_id_uindex
        unique (id)
);

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

create table permissions
(
    id       int auto_increment
        primary key,
    identity int          not null,
    perms    varchar(100) not null,
    constraint permissions_id_uindex
        unique (id)
);

create table user
(
    id          int auto_increment
        primary key,
    username    varchar(100)  not null,
    password    varchar(100)  not null,
    email       varchar(100)  not null,
    create_time datetime      not null,
    uuid        varchar(100)  not null,
    identity    int default 1 not null comment '1普通用户0管理员2vip用户',
    status      int default 1 not null comment '1启用0禁用',
    size        int default 0 not null,
    visit_size  int default 0 not null,
    sign_in     int default 0 not null comment '0今日未签到1今日已签到',
    constraint user_id_uindex
        unique (id)
);

create table user_privilege
(
    id            int auto_increment
        primary key,
    max_size      int null comment 'api最大调用次数',
    api_id        int null comment 'api的id',
    user_identity int not null,
    constraint user_privilege_id_uindex
        unique (id)
);


