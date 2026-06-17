Feature: Registrar cartões amarelos e vermelhos da partida

Scenario: Registrar cartão amarelo e cartão vermelho em uma partida
    Given que existe uma partida cadastrada em um torneio
    And que existem jogadores vinculados aos times participantes da partida
    When o organizador registrar que o Jogador A recebeu um cartão amarelo
    And registrar que o Jogador B recebeu um cartão vermelho
    Then o sistema deve registrar os cartões da partida corretamente
    And o cartão amarelo deve ser associado ao Jogador A
    And o cartão vermelho deve ser associado ao Jogador B
