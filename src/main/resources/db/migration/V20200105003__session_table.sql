create table Session (
    id bigint not null,
    latitude double precision,
    longitude double precision,
    updatedAt datetime(6),
    uuid varchar(255),
    primary key (id)
) engine=InnoDB;

alter table Session add constraint UK_uuid unique (uuid);