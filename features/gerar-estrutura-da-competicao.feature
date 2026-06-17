Feature: Gerar estrutura da competição

  As a organizador do torneio
  I want gerar a estrutura da competição
  So that eu possa visualizar grupos, tabela ou chaveamento conforme o formato escolhido

  Scenario: Gerar estrutura de torneio mata-mata
    Given que existe um torneio com formato mata-mata
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competição
    Then o sistema deve gerar o chaveamento eliminatório

  Scenario: Gerar estrutura de torneio por pontos corridos
    Given que existe um torneio com formato pontos corridos
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competição
    Then o sistema deve gerar a estrutura de tabela da competição

  Scenario: Gerar estrutura de torneio com fase de grupos e mata-mata
    Given que existe um torneio com formato fase de grupos com mata-mata
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competição
    Then o sistema deve gerar os grupos da competição

  Scenario: Impedir geração de estrutura sem participantes suficientes
    Given que existe um torneio configurado
    And que o torneio não possui participantes suficientes
    When o organizador gerar a estrutura da competição
    Then o sistema deve impedir a geração da estrutura