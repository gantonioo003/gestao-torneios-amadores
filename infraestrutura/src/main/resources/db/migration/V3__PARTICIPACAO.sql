-- Conta de usuário
create table CONTA_USUARIO (
    ID      bigint not null,
    NOME    varchar not null,
    EMAIL   varchar not null unique,
    SENHA   varchar not null,
    TIPO    varchar not null,
    primary key (ID)
);

-- Time
create table TIME (
    ID              bigint not null,
    NOME            varchar not null,
    RESPONSAVEL_ID  bigint not null,
    primary key (ID),
    foreign key (RESPONSAVEL_ID) references CONTA_USUARIO(ID)
);

-- Torneios vinculados ao time
create table TIME_TORNEIO (
    TIME_ID     bigint not null,
    TORNEIO_ID  bigint not null,
    primary key (TIME_ID, TORNEIO_ID),
    foreign key (TIME_ID) references TIME(ID)
);

-- Profissional esportivo
create table PROFISSIONAL_ESPORTIVO (
    ID              bigint not null,
    NOME            varchar not null,
    TIPO            varchar not null,
    CADASTRANTE_ID  bigint not null,
    primary key (ID),
    foreign key (CADASTRANTE_ID) references CONTA_USUARIO(ID)
);

-- Registro de carreira do profissional
create table REGISTRO_DE_CARREIRA (
    ID               bigint not null,
    PROFISSIONAL_ID  bigint not null,
    NOME_DO_CLUBE    varchar not null,
    DATA_INICIO      date not null,
    DATA_FIM         date,
    MOTIVO_DE_SAIDA  varchar,
    primary key (ID),
    foreign key (PROFISSIONAL_ID) references PROFISSIONAL_ESPORTIVO(ID)
);

-- Vínculo de profissional ao time (elenco/comissão)
create table VINCULO_PROFISSIONAL (
    ID                    bigint not null,
    TIME_ID               bigint not null,
    PROFISSIONAL_ID       bigint not null,
    FUNCAO                varchar not null,
    DATA_INICIO           date not null,
    DATA_LIMITE_CONTRATO  date,
    primary key (ID),
    unique (TIME_ID, PROFISSIONAL_ID),
    foreign key (TIME_ID) references TIME(ID),
    foreign key (PROFISSIONAL_ID) references PROFISSIONAL_ESPORTIVO(ID)
);

-- Solicitação de participação em torneio
create table SOLICITACAO_PARTICIPACAO (
    ID              bigint not null,
    SOLICITANTE_ID  bigint not null,
    TIME_ID         bigint not null,
    TORNEIO_ID      bigint not null,
    STATUS          varchar not null,
    primary key (ID),
    foreign key (SOLICITANTE_ID) references CONTA_USUARIO(ID),
    foreign key (TIME_ID) references TIME(ID)
);
