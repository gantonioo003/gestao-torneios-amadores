Feature: Gerenciar elenco de jogadores de um time

  As a usuÃ¡rio responsÃ¡vel por um time
  I want gerenciar o elenco de jogadores
  So that eu possa formar a equipe que disputarÃ¡ torneios

  Scenario: Adicionar jogador ao elenco do time
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele Ã© responsÃ¡vel por um time
    When ele adicionar um jogador com dados vÃ¡lidos ao elenco
    Then o sistema deve registrar o jogador no time

  Scenario: Editar dados de um jogador do elenco
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele Ã© responsÃ¡vel por um time
    And que o time possui jogadores cadastrados
    When ele alterar os dados de um jogador do elenco
    Then o sistema deve atualizar os dados do jogador

  Scenario: Remover jogador do elenco do time
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele Ã© responsÃ¡vel por um time
    And que o time possui jogadores cadastrados
    When ele solicitar a remoÃ§Ã£o de um jogador do elenco
    Then o sistema deve remover o jogador do time

  Scenario: Impedir gerenciamento do elenco por usuÃ¡rio nÃ£o responsÃ¡vel
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele nÃ£o Ã© responsÃ¡vel pelo time
    When ele tentar adicionar um jogador ao elenco
    Then o sistema deve impedir a operaÃ§Ã£o
