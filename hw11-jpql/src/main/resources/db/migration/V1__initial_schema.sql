-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
/*
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

 */

-- Для @GeneratedValue(strategy = GenerationType.SEQUENCE)
create sequence address_seq start with 1 increment by 1;
create sequence client_seq start with 1 increment by 1;
create sequence phone_seq start with 1 increment by 1;

create table address
(
    id     bigint not null,
    street varchar(255),
    primary key (id)
);

create table client
(
    address_id bigint unique,
    id         bigint not null,
    name       varchar(255),
    primary key (id),
    foreign key (address_id) references address (id)
);

create table phone
(
    client_id bigint,
    id        bigint not null,
    number    varchar(255),
    primary key (id),
    foreign key (client_id) references client (id)
);
