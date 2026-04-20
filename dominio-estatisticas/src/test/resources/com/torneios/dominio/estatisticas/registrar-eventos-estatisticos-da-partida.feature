Feature: Registrar eventos estatísticos da partida

  As a organizador do torneio
  I want registrar eventos estatísticos da partida
  So that o sistema possa calcular o desempenho dos jogadores

  Scenario: Registrar gol e assistência em uma partida
    Given que existe uma partida cadastrada
    And que o usuário autenticado é o organizador
    When ele registrar um gol e uma assistência para jogadores
    Then o sistema deve armazenar os eventos corretamente

  Scenario: Registrar cartões em uma partida
    Given que existe uma partida cadastrada
    And que o usuário autenticado é o organizador
    When ele registrar cartão amarelo ou vermelho para jogadores
    Then o sistema deve armazenar os eventos corretamente

  Scenario: Impedir registro de eventos por usuário não organizador
    Given que existe uma partida cadastrada
    And que o usuário autenticado não é o organizador
    When ele tentar registrar eventos da partida
    Then o sistema deve impedir a operação

  Scenario: Impedir registro de eventos para jogador não pertencente ao time
    Given que existe uma partida cadastrada
    And que o jogador não pertence aos times da partida
    When o organizador tentar registrar um evento para esse jogador
    Then o sistema deve impedir o registro