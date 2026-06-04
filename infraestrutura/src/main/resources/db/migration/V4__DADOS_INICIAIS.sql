-- Usuário padrão para desenvolvimento/testes
-- ID=1, senha=123456 (sem hash pois AutenticacaoServico não valida senha ainda)
INSERT IGNORE INTO CONTA_USUARIO (ID, NOME, EMAIL, SENHA, TIPO)
VALUES (1, 'Usuário Teste', 'usuario@torneios.com', '123456', 'JOGADOR');
