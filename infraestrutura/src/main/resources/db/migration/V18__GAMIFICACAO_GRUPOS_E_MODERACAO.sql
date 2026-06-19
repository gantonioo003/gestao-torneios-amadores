create table if not exists PROGRESSO_PALPITE (
    USUARIO_ID bigint not null,
    PONTOS integer not null default 0,
    SEQUENCIA_ATUAL integer not null default 0,
    MAIOR_SEQUENCIA integer not null default 0,
    TOTAL_PALPITES integer not null default 0,
    TOTAL_ACERTOS integer not null default 0,
    ULTIMA_PARTICIPACAO date null,
    SELOS_DATA longtext null,
    primary key (USUARIO_ID),
    constraint FK_PROGRESSO_PALPITE_USUARIO foreign key (USUARIO_ID) references CONTA_USUARIO(ID)
);

create table if not exists GRUPO_CHAT (
    ID bigint not null,
    NOME varchar(80) not null,
    CRIADOR_ID bigint not null,
    CRIADO_EM datetime(6) not null,
    PARTICIPANTES_DATA longtext not null,
    CONVITES_PENDENTES_DATA longtext null,
    MENSAGENS_DATA longtext null,
    primary key (ID),
    constraint FK_GRUPO_CHAT_CRIADOR foreign key (CRIADOR_ID) references CONTA_USUARIO(ID)
);

create table if not exists DENUNCIA (
    ID bigint not null,
    DENUNCIANTE_ID bigint not null,
    TIPO_ALVO varchar(30) not null,
    ALVO_ID bigint not null,
    MOTIVO varchar(500) not null,
    STATUS varchar(30) not null,
    CRIADA_EM datetime(6) not null,
    primary key (ID),
    index IDX_DENUNCIA_STATUS (STATUS, CRIADA_EM),
    constraint FK_DENUNCIA_USUARIO foreign key (DENUNCIANTE_ID) references CONTA_USUARIO(ID)
);
