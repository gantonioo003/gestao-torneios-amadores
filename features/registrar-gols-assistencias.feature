Feature: Registrar gols e assistências da partida

Scenario: Registrar gol e assistência em uma partida
    Given que existe uma partida cadastrada em um torneio
    And que existem jogadores vinculados aos times participantes da partida
    When o organizador registrar que o Jogador A marcou um gol
    And registrar que o Jogador B realizou uma assistência
    Then o sistema deve registrar os eventos da partida corretamente
    And o gol deve ser associado ao Jogador A
    And a assistência deve ser associada ao Jogador B