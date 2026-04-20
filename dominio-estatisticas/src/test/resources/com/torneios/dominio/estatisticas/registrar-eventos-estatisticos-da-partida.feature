Feature: Registrar eventos estatÃ­sticos da partida

  As a organizador do torneio
  I want registrar eventos estatÃ­sticos da partida
  So that o sistema possa calcular o desempenho dos jogadores

  Scenario: Registrar gol e assistÃªncia em uma partida
    Given que existe uma partida cadastrada
    And que o usuÃ¡rio autenticado Ã© o organizador
    When ele registrar um gol e uma assistÃªncia para jogadores
    Then o sistema deve armazenar os eventos corretamente

  Scenario: Registrar cartÃµes em uma partida
    Given que existe uma partida cadastrada
    And que o usuÃ¡rio autenticado Ã© o organizador
    When ele registrar cartÃ£o amarelo ou vermelho para jogadores
    Then o sistema deve armazenar os eventos corretamente

  Scenario: Impedir registro de eventos por usuÃ¡rio nÃ£o organizador
    Given que existe uma partida cadastrada
    And que o usuÃ¡rio autenticado nÃ£o Ã© o organizador
    When ele tentar registrar eventos da partida
    Then o sistema deve impedir a operaÃ§Ã£o

  Scenario: Impedir registro de eventos para jogador nÃ£o pertencente ao time
    Given que existe uma partida cadastrada
    And que o jogador nÃ£o pertence aos times da partida
    When o organizador tentar registrar um evento para esse jogador
    Then o sistema deve impedir o registro
