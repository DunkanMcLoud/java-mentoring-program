--drop table if exists event CASCADE;

create table event
(
    id         varchar(255) not null,
    date_time  timestamp,
    event_type varchar(255),
    place      varchar(255),
    speaker    varchar(255),
    title      varchar(255),
    primary key (id)
);
