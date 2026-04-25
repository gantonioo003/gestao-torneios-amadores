# Mapeado para F6: Gerenciar elenco de jogadores de um time
Feature: Gerenciar elenco de jogadores do time

  As a usuário responsável por um time
  I want gerenciar o elenco de jogadores do time
  So that eu possa manter a equipe atualizada para os torneios

  Scenario: Responsável adiciona jogador ao elenco com dados válidos
    Given que o usuário está autenticado
    And que ele é responsável por um time
    When ele adicionar um jogador com dados válidos ao elenco
    Then o sistema deve registrar o jogador no elenco do time

  Scenario: Responsável edita dados de jogador existente no elenco
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui jogadores cadastrados
    When ele alterar o nome de um jogador do elenco
    Then o sistema deve atualizar os dados do jogador

  Scenario: Responsável remove jogador do elenco
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui jogadores cadastrados
    When ele solicitar a remoção de um jogador do elenco
    Then o sistema deve remover o jogador do time

  Scenario: Usuário não responsável tenta adicionar jogador ao elenco
    Given que o usuário está autenticado
    And que ele não é responsável pelo time
    When ele tentar adicionar um jogador ao elenco
    Then o sistema deve impedir a operação

  Scenario: Responsável tenta remover jogador inexistente do elenco
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time não possui jogadores cadastrados
    When ele tentar remover um jogador do elenco
    Then o sistema deve informar que o jogador não foi encontrado no elenco
