alter table NOTIFICACAO_PARTICIPACAO
    modify column CATEGORIA varchar(30) not null default 'SISTEMA',
    modify column TITULO varchar(160) not null,
    modify column MENSAGEM varchar(500) not null,
    modify column LINK varchar(500) not null;
