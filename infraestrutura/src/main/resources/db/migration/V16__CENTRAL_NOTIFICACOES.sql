alter table NOTIFICACAO_PARTICIPACAO
    add column CATEGORIA varchar(30) not null default 'SISTEMA',
    add column ARQUIVADA bit not null default 0;

create index IDX_NOTIFICACAO_USUARIO_ESTADO
    on NOTIFICACAO_PARTICIPACAO (USUARIO_ID, ARQUIVADA, LIDA, CRIADA_EM);

create table if not exists PREFERENCIA_NOTIFICACAO (
    USUARIO_ID bigint not null,
    TORNEIO bit not null default 1,
    TIME bit not null default 1,
    AMISTOSO bit not null default 1,
    SOCIAL bit not null default 1,
    SISTEMA bit not null default 1,
    primary key (USUARIO_ID)
);
