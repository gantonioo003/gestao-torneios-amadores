-- Usuário padrão para desenvolvimento/testes
-- ID=1, senha=123456
INSERT IGNORE INTO CONTA_USUARIO (ID, NOME, EMAIL, SENHA, TIPO)
VALUES (1, 'Usuário Teste', 'usuario@torneios.com', '123456', 'JOGADOR');

-- Torneios de exemplo
INSERT IGNORE INTO TORNEIO (ID, NOME, FORMATO, FORMATO_EQUIPE, ORGANIZADOR_ID, ACEITA_SOLICITACOES, STATUS)
VALUES
  (5004, 'Copa da Cidade 2025',  'PONTOS_CORRIDOS',              'ONZE_JOGADORES', 1, 1, 'CONFIGURADO'),
  (5005, 'Liga Amadora Plus',    'FASE_DE_GRUPOS_COM_MATA_MATA', 'ONZE_JOGADORES', 1, 1, 'CONFIGURADO'),
  (5006, 'Torneio Relâmpago FC', 'MATA_MATA',                    'ONZE_JOGADORES', 1, 0, 'INICIADO');

-- Vínculos do time Águias F (ID=1001) com 2 torneios
INSERT IGNORE INTO TIME_TORNEIO (TIME_ID, TORNEIO_ID) VALUES (1001, 5004), (1001, 5005);
