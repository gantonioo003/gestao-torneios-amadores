create table if not exists CONTA_TORNEIO_SALVO (
    CONTA_ID bigint not null,
    TORNEIO_ID bigint not null,
    primary key (CONTA_ID, TORNEIO_ID),
    constraint FK_CONTA_TORNEIO_SALVO_CONTA
        foreign key (CONTA_ID) references CONTA_USUARIO(ID),
    constraint FK_CONTA_TORNEIO_SALVO_TORNEIO
        foreign key (TORNEIO_ID) references TORNEIO(ID)
);
