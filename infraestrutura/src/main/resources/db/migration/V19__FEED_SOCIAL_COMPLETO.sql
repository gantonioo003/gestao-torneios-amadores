alter table PUBLICACAO_FEED
    add column TIPO_IDENTIDADE varchar(30) null,
    add column IDENTIDADE_ID bigint null,
    add column PUBLICACAO_PAI_ID bigint null,
    add column CRIADA_EM datetime(6) null;

update PUBLICACAO_FEED
set TIPO_IDENTIDADE = case
    when TIPO = 'ATUALIZACAO_AUTOMATICA' then 'SISTEMA'
    when TIPO = 'COMUNICADO_OFICIAL' then 'TORNEIO'
    else 'USUARIO'
end,
IDENTIDADE_ID = case
    when TIPO = 'ATUALIZACAO_AUTOMATICA' then PARTIDA_ID
    when TIPO = 'COMUNICADO_OFICIAL' then TORNEIO_ID
    else AUTOR_ID
end,
CRIADA_EM = current_timestamp(6)
where TIPO_IDENTIDADE is null;

create index IDX_PUBLICACAO_AUTOR_DATA on PUBLICACAO_FEED (AUTOR_ID, CRIADA_EM);
create index IDX_PUBLICACAO_PAI_DATA on PUBLICACAO_FEED (PUBLICACAO_PAI_ID, CRIADA_EM);
