create table if not exists NOTIFICACAO_PARTICIPACAO (
    ID bigint not null,
    USUARIO_ID bigint not null,
    TITULO varchar(160) not null,
    MENSAGEM varchar(500) not null,
    LINK varchar(500) not null,
    LIDA bit not null,
    CRIADA_EM datetime(6) not null,
    primary key (ID),
    index IDX_NOTIFICACAO_USUARIO_DATA (USUARIO_ID, CRIADA_EM)
);
