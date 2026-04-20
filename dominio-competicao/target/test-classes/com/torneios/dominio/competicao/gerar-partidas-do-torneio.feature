Feature: Gerar partidas do torneio

  As a organizador do torneio
  I want gerar as partidas do torneio
  So that a competiÃ§Ã£o possa ser disputada

  Scenario: Gerar partidas para torneio por pontos corridos
    Given que existe um torneio com formato pontos corridos
    And que a estrutura da competiÃ§Ã£o jÃ¡ foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve registrar as partidas da competiÃ§Ã£o

  Scenario: Gerar partidas para torneio mata-mata
    Given que existe um torneio com formato mata-mata
    And que a estrutura da competiÃ§Ã£o jÃ¡ foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve registrar as partidas do chaveamento

  Scenario: Gerar partidas para torneio com fase de grupos
    Given que existe um torneio com fase de grupos
    And que a estrutura da competiÃ§Ã£o jÃ¡ foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve registrar as partidas da fase de grupos

  Scenario: Impedir geraÃ§Ã£o de partidas sem estrutura prÃ©via da competiÃ§Ã£o
    Given que existe um torneio configurado
    And que a estrutura da competiÃ§Ã£o ainda nÃ£o foi gerada
    When o organizador gerar as partidas do torneio
    Then o sistema deve impedir a operaÃ§Ã£o
