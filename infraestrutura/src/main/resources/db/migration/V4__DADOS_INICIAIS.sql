insert ignore into CONTA_USUARIO (ID, NOME, EMAIL, SENHA, TIPO)
values (1, 'Usuario Teste', 'usuario@torneios.com', '123456', 'JOGADOR');

insert ignore into TORNEIO (
    ID,
    NOME,
    FORMATO,
    FORMATO_EQUIPE,
    ORGANIZADOR_ID,
    ACEITA_SOLICITACOES,
    EDICAO_ATUAL,
    STATUS,
    HISTORICO_EDICOES_DATA
) values
    (5004, 'Copa da Cidade 2025', 'PONTOS_CORRIDOS', 'ONZE_JOGADORES', 1, 1, 1, 'CONFIGURADO', null),
    (5005, 'Liga Amadora Plus', 'FASE_DE_GRUPOS_COM_MATA_MATA', 'ONZE_JOGADORES', 1, 1, 1, 'CONFIGURADO', null),
    (5006, 'Torneio Relampago FC', 'MATA_MATA', 'ONZE_JOGADORES', 1, 0, 1, 'INICIADO', null);
