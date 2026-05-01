Feature: Gerenciar elenco e comissao tecnica do time

  As a usuario responsavel por um time
  I want adicionar, editar e remover jogadores e tecnicos
  So that o time mantenha sua composicao atualizada para torneios e partidas

  Scenario: Adicionar jogador ao elenco do time
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    When ele adicionar um jogador com dados validos ao elenco
    Then o sistema deve registrar o jogador no elenco do time

  Scenario: Editar dados de um jogador do elenco
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que o time possui jogadores cadastrados
    When ele alterar o nome de um jogador do elenco
    Then o sistema deve atualizar os dados do jogador

  Scenario: Remover jogador do elenco do time
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que o time possui jogadores cadastrados
    When ele solicitar a remocao de um jogador do elenco
    Then o sistema deve remover o jogador do time

  Scenario: Associar tecnico a um time
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    When ele associar um tecnico com dados validos ao time
    Then o sistema deve registrar o tecnico na comissao tecnica do time

  Scenario: Editar dados de um tecnico do time
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que o time possui tecnico associado
    When ele alterar o nome do tecnico
    Then o sistema deve atualizar os dados do tecnico

  Scenario: Remover tecnico da comissao tecnica do time
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que o time possui tecnico associado
    When ele solicitar a remocao do tecnico
    Then o sistema deve remover o tecnico da comissao tecnica do time

  Scenario: Impedir gerenciamento por usuario nao responsavel
    Given que o usuario esta autenticado
    And que ele nao e responsavel pelo time
    When ele tentar adicionar um jogador ao elenco
    Then o sistema deve impedir a operacao

  Scenario: Responsavel tenta remover jogador inexistente do elenco
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que o time nao possui jogadores cadastrados
    When ele tentar remover um jogador do elenco
    Then o sistema deve informar que o jogador nao foi encontrado no elenco

  Scenario: Responsavel tenta remover tecnico quando nao ha tecnico associado
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que o time nao possui tecnico associado
    When ele tentar remover o tecnico do time
    Then o sistema deve informar que nao existe tecnico associado ao time
