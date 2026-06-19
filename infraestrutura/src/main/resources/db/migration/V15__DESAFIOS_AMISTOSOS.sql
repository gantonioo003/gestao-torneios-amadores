create table if not exists DESAFIO_AMISTOSO (
    ID bigint not null,
    PROPONENTE_ID bigint not null,
    TIME_DESAFIANTE_ID bigint not null,
    TIME_DESAFIADO_ID bigint not null,
    DATA_HORA datetime(6) not null,
    LOCAL varchar(255) not null,
    STATUS varchar(50) not null,
    GOLS_DESAFIANTE integer null,
    GOLS_DESAFIADO integer null,
    primary key (ID),
    index IDX_DESAFIO_TIME_DESAFIANTE (TIME_DESAFIANTE_ID),
    index IDX_DESAFIO_TIME_DESAFIADO (TIME_DESAFIADO_ID),
    constraint FK_DESAFIO_PROPONENTE foreign key (PROPONENTE_ID) references CONTA_USUARIO(ID),
    constraint FK_DESAFIO_TIME_DESAFIANTE foreign key (TIME_DESAFIANTE_ID) references TIME(ID),
    constraint FK_DESAFIO_TIME_DESAFIADO foreign key (TIME_DESAFIADO_ID) references TIME(ID)
);
