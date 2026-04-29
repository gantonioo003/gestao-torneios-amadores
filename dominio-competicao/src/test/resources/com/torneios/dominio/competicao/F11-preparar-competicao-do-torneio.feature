Feature: Preparar competicao do torneio

  As a organizador do torneio
  I want preparar a estrutura, as rodadas e as partidas do torneio
  So that a competicao possa ser disputada conforme o formato definido

  Scenario: Preparar competicao por pontos corridos
    Given que existe um torneio com formato pontos corridos
    And que a estrutura da competicao ja foi gerada
    When o organizador preparar a competicao do torneio
    Then o sistema deve registrar as partidas e rodadas da competicao

  Scenario: Preparar competicao mata-mata
    Given que existe um torneio com formato mata-mata
    And que a estrutura da competicao ja foi gerada
    When o organizador preparar a competicao do torneio
    Then o sistema deve registrar as partidas do chaveamento

  Scenario: Preparar competicao com fase de grupos
    Given que existe um torneio com fase de grupos
    And que a estrutura da competicao ja foi gerada
    When o organizador preparar a competicao do torneio
    Then o sistema deve registrar as partidas da fase de grupos

  Scenario: Impedir preparacao sem estrutura previa da competicao
    Given que existe um torneio configurado
    And que a estrutura da competicao ainda nao foi gerada
    When o organizador preparar a competicao do torneio
    Then o sistema deve impedir a operacao
