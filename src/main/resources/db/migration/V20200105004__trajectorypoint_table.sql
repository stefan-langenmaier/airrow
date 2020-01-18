create table TrajectoryPoint (
    id bigint not null,
    direction double precision,
    latitude double precision,
    longitude double precision,
    updatedAt datetime(6),
    sessionId bigint,
    primary key (id)
) engine=InnoDB;