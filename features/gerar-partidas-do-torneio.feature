Feature: Gerar partidas do torneio

  As a organizador do torneio
  I want gerar as partidas do torneio
  So that a competição possa ser disputada

  Scenario: Gerar partidas para torneio por pontos corridos
    Given que existe um torneio com formato pontos corridos
    And que a estrutura da competição já foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve registrar as partidas da competição

  Scenario: Gerar partidas para torneio mata-mata
    Given que existe um torneio com formato mata-mata
    And que a estrutura da competição já foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve registrar as partidas do chaveamento

  Scenario: Gerar partidas para torneio com fase de grupos
    Given que existe um torneio com fase de grupos
    And que a estrutura da competição já foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve registrar as partidas da fase de grupos

  Scenario: Impedir geração de partidas sem estrutura prévia da competição
    Given que existe um torneio configurado
    And que a estrutura da competição ainda não foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve impedir a operação