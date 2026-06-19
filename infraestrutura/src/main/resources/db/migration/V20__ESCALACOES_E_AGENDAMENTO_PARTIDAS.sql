alter table PARTIDA
    add column DATA_HORA_AGENDADA datetime(6) null,
    add column LOCAL_PARTIDA varchar(180) null;

create table if not exists ESCALACAO (
    ID bigint not null,
    PARTIDA_ID bigint not null,
    TIME_ID bigint not null,
    FORMATO_EQUIPE varchar(40) not null,
    TIPO_VISUALIZACAO varchar(40) not null,
    ESQUEMA_TATICO varchar(60) null,
    CONGELADA boolean not null default false,
    primary key (ID),
    unique key UK_ESCALACAO_PARTIDA_TIME (PARTIDA_ID, TIME_ID),
    constraint FK_ESCALACAO_PARTIDA foreign key (PARTIDA_ID) references PARTIDA(ID),
    constraint FK_ESCALACAO_TIME foreign key (TIME_ID) references TIME(ID)
);

create table if not exists ESCALACAO_TITULAR (
    ID bigint not null,
    ESCALACAO_ID bigint not null,
    JOGADOR_ID bigint not null,
    POSICAO varchar(40) not null,
    ORDEM integer not null,
    primary key (ID),
    constraint FK_ESCALACAO_TITULAR foreign key (ESCALACAO_ID) references ESCALACAO(ID)
);

create table if not exists ESCALACAO_RESERVA (
    ID bigint not null,
    ESCALACAO_ID bigint not null,
    JOGADOR_ID bigint not null,
    ORDEM integer not null,
    primary key (ID),
    constraint FK_ESCALACAO_RESERVA foreign key (ESCALACAO_ID) references ESCALACAO(ID)
);
