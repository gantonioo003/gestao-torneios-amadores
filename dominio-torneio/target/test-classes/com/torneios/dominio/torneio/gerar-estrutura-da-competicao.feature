Feature: Gerar estrutura da competiÃ§Ã£o

  As a organizador do torneio
  I want gerar a estrutura da competiÃ§Ã£o
  So that eu possa visualizar grupos, tabela ou chaveamento conforme o formato escolhido

  Scenario: Gerar estrutura de torneio mata-mata
    Given que existe um torneio com formato mata-mata
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competiÃ§Ã£o
    Then o sistema deve gerar o chaveamento eliminatÃ³rio

  Scenario: Gerar estrutura de torneio por pontos corridos
    Given que existe um torneio com formato pontos corridos
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competiÃ§Ã£o
    Then o sistema deve gerar a estrutura de tabela da competiÃ§Ã£o

  Scenario: Gerar estrutura de torneio com fase de grupos e mata-mata
    Given que existe um torneio com formato fase de grupos com mata-mata
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competiÃ§Ã£o
    Then o sistema deve gerar os grupos da competiÃ§Ã£o

  Scenario: Impedir geraÃ§Ã£o de estrutura sem participantes suficientes
    Given que existe um torneio configurado
    And que o torneio nÃ£o possui participantes suficientes
    When o organizador gerar a estrutura da competiÃ§Ã£o
    Then o sistema deve impedir a geraÃ§Ã£o da estrutura
