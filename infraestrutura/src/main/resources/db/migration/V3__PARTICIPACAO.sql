create table if not exists CONTA_USUARIO (
    ID bigint not null,
    NOME varchar(255) not null,
    EMAIL varchar(255) not null unique,
    SENHA varchar(255) not null,
    TIPO varchar(50) not null,
    primary key (ID)
);

create table if not exists TIME (
    ID bigint not null,
    NOME varchar(255) not null,
    RESPONSAVEL_ID bigint not null,
    primary key (ID),
    constraint FK_TIME_RESPONSAVEL foreign key (RESPONSAVEL_ID) references CONTA_USUARIO(ID)
);

create table if not exists TORNEIO (
    ID bigint not null,
    NOME varchar(255) not null,
    FORMATO varchar(80) not null,
    FORMATO_EQUIPE varchar(80) not null,
    ORGANIZADOR_ID bigint not null,
    ACEITA_SOLICITACOES bit not null,
    EDICAO_ATUAL integer null,
    STATUS varchar(50) not null,
    HISTORICO_EDICOES_DATA longtext null,
    primary key (ID),
    constraint FK_TORNEIO_ORGANIZADOR foreign key (ORGANIZADOR_ID) references CONTA_USUARIO(ID)
);

create table if not exists TORNEIO_PARTICIPANTE (
    TORNEIO_ID bigint not null,
    TIME_ID bigint not null
);

create table if not exists TIME_TORNEIO (
    TIME_ID bigint not null,
    TORNEIO_ID bigint not null,
    primary key (TIME_ID, TORNEIO_ID),
    constraint FK_TIME_TORNEIO_TIME foreign key (TIME_ID) references TIME(ID)
);

create table if not exists PROFISSIONAL_ESPORTIVO (
    ID bigint not null,
    NOME varchar(255) not null,
    TIPO varchar(50) not null,
    CADASTRANTE_ID bigint not null,
    primary key (ID),
    constraint FK_PROFISSIONAL_CADASTRANTE foreign key (CADASTRANTE_ID) references CONTA_USUARIO(ID)
);

create table if not exists REGISTRO_DE_CARREIRA (
    ID bigint not null,
    PROFISSIONAL_ID bigint not null,
    NOME_DO_CLUBE varchar(255) not null,
    DATA_INICIO date not null,
    DATA_FIM date null,
    MOTIVO_DE_SAIDA varchar(255) null,
    DESCRICAO varchar(1000) null,
    primary key (ID),
    constraint FK_REGISTRO_CARREIRA_PROFISSIONAL foreign key (PROFISSIONAL_ID) references PROFISSIONAL_ESPORTIVO(ID)
);

create table if not exists VINCULO_PROFISSIONAL (
    ID bigint not null,
    TIME_ID bigint not null,
    PROFISSIONAL_ID bigint not null,
    FUNCAO varchar(50) not null,
    DATA_INICIO date not null,
    DATA_LIMITE_CONTRATO date null,
    primary key (ID),
    unique key UK_TIME_PROFISSIONAL (TIME_ID, PROFISSIONAL_ID),
    constraint FK_VINCULO_TIME foreign key (TIME_ID) references TIME(ID),
    constraint FK_VINCULO_PROFISSIONAL foreign key (PROFISSIONAL_ID) references PROFISSIONAL_ESPORTIVO(ID)
);

create table if not exists SOLICITACAO_PARTICIPACAO (
    ID bigint not null,
    SOLICITANTE_ID bigint not null,
    TIME_ID bigint not null,
    TORNEIO_ID bigint not null,
    STATUS varchar(50) not null,
    primary key (ID),
    constraint FK_SOLICITACAO_USUARIO foreign key (SOLICITANTE_ID) references CONTA_USUARIO(ID),
    constraint FK_SOLICITACAO_TIME foreign key (TIME_ID) references TIME(ID)
);
