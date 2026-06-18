alter table CONTA_USUARIO
    add column NOME_USUARIO varchar(30) null,
    add column TELEFONE varchar(25) null,
    add column DATA_NASCIMENTO date null,
    add column CIDADE varchar(100) null,
    add column ESTADO varchar(40) null,
    add column BIOGRAFIA varchar(300) null,
    add column FOTO_PERFIL_URL varchar(500) null,
    add column PROVEDOR varchar(30) not null default 'LOCAL';

update CONTA_USUARIO
set NOME_USUARIO = concat(
        left(
            replace(replace(replace(substring_index(EMAIL, '@', 1), '-', ''), '+', ''), ' ', ''),
            14
        ),
        '_',
        right(ID, 12)
    )
where NOME_USUARIO is null;

alter table CONTA_USUARIO
    modify column NOME_USUARIO varchar(30) not null,
    add unique key UK_CONTA_USUARIO_NOME_USUARIO (NOME_USUARIO);
