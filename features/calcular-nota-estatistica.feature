Feature: Calcular nota estatística do jogador

Scenario: Calcular nota com base em eventos da partida e pesos definidos
    Given que existe uma fórmula de cálculo da nota estatística
    And que a nota base do jogador é 5.0
    And o peso do gol é 2.0
    And o peso da assistência é 1.0
    And o peso do cartão amarelo é -1.0
    And que foi registrado 1 gol para esse jogador
    And que foi registrada 1 assistência para esse jogador
    And que foi registrado 1 cartão amarelo para esse jogador
    When o sistema calcular a nota estatística do jogador
    Then a nota final do jogador deve ser 7.0
