create table if not exists conversa_privada (
    id bigint not null,
    solicitante_id bigint not null,
    destinatario_id bigint not null,
    status varchar(50) not null,
    solicitada_em datetime(6) not null,
    mensagens_data longtext null,
    primary key (id),
    index IDX_CONVERSA_SOLICITANTE_STATUS (solicitante_id, status),
    index IDX_CONVERSA_DESTINATARIO_STATUS (destinatario_id, status)
);
