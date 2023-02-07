create table api
(
    id   int auto_increment
        primary key,
    name varchar(100)  not null comment 'api的名字',
    size int default 0 null comment '总调用次数',
    constraint api_id_uindex
        unique (id)
);

create table authentication
(
    id          int auto_increment
        primary key,
    username    varchar(100) not null,
    name        varchar(100) not null,
    id_card     varchar(100) not null,
    create_time datetime     not null,
    constraint authentication_id_uindex
        unique (id)
);

create table context
(
    id          int auto_increment
        primary key,
    uid         int                                                                                        not null,
    name        varchar(100)                                                                               not null,
    email       varchar(200)                                                                               not null,
    context     longtext                                                                                   null,
    address     varchar(100)                                                                               not null,
    create_time datetime                                                                                   not null,
    ip          varchar(100)                                                                               null,
    thumbs_up   int          default 0                                                                     not null comment '点赞',
    avatar      varchar(256) default 'https://bpic.51yuansu.com/pic2/cover/00/43/85/58139fe3a6e09_610.jpg' null,
    constraint context_id_uindex
        unique (id)
);

create index context_thumbs_up_uid_index
    on context (thumbs_up, uid);

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
    id             int auto_increment
        primary key,
    username       varchar(100)                           not null,
    password       varchar(100)                           not null,
    email          varchar(100)                           not null,
    create_time    datetime                               not null,
    uuid           varchar(100)                           not null,
    identity       int      default 1                     not null comment '1普通用户0管理员2vip用户',
    status         int      default 1                     not null comment '1启用0禁用',
    size           int      default 0                     not null,
    visit_size     int      default 0                     not null,
    sign_in        int      default 0                     not null comment '0今日未签到1今日已签到',
    last_login     datetime default '1971-01-02 00:00:00' not null,
    authentication int      default 0                     not null comment '是否实名认证成功 0未实名 1已实名',
    constraint user_email_uindex
        unique (email),
    constraint user_id_uindex
        unique (id),
    constraint user_username_uindex
        unique (username)
);

create index user_uuid_index
    on user (uuid);

create table user_info
(
    username  varchar(100)             not null
        primary key,
    thumbs_up int          default 0   not null,
    integral  int          default 0   not null,
    Diamonds  int          default 0   not null,
    url       varchar(200) default '0' not null,
    constraint user_info_username_uindex
        unique (username)
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


