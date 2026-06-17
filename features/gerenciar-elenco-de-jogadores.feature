Feature: Gerenciar elenco de jogadores de um time

  As a usuário responsável por um time
  I want gerenciar o elenco de jogadores
  So that eu possa formar a equipe que disputará torneios

  Scenario: Adicionar jogador ao elenco do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    When ele adicionar um jogador com dados válidos ao elenco
    Then o sistema deve registrar o jogador no time

  Scenario: Editar dados de um jogador do elenco
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui jogadores cadastrados
    When ele alterar os dados de um jogador do elenco
    Then o sistema deve atualizar os dados do jogador

  Scenario: Remover jogador do elenco do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui jogadores cadastrados
    When ele solicitar a remoção de um jogador do elenco
    Then o sistema deve remover o jogador do time

  Scenario: Impedir gerenciamento do elenco por usuário não responsável
    Given que o usuário está autenticado
    And que ele não é responsável pelo time
    When ele tentar adicionar um jogador ao elenco
    Then o sistema deve impedir a operação