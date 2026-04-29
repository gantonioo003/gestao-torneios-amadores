Feature: Gerenciar sumula estatistica da partida

  As a organizador do torneio
  I want registrar, corrigir e remover eventos estatisticos opcionais da partida
  So that a sumula possa alimentar estatisticas e rankings quando houver dados detalhados

  Scenario: Registrar gol e assistencia em uma partida
    Given que existe uma partida cadastrada
    And que o usuario autenticado e o organizador
    When ele registrar um gol e uma assistencia para jogadores
    Then o sistema deve armazenar os eventos corretamente

  Scenario: Registrar cartoes em uma partida
    Given que existe uma partida cadastrada
    And que o usuario autenticado e o organizador
    When ele registrar cartao amarelo ou vermelho para jogadores
    Then o sistema deve armazenar os eventos corretamente

  Scenario: Corrigir evento estatistico da sumula
    Given que existe um evento estatistico registrado na sumula da partida
    And que o usuario autenticado e o organizador
    When ele corrigir o evento estatistico da sumula
    Then o sistema deve atualizar o evento estatistico da partida

  Scenario: Remover evento estatistico da sumula
    Given que existe um evento estatistico registrado na sumula da partida
    And que o usuario autenticado e o organizador
    When ele remover o evento estatistico da sumula
    Then o sistema deve retirar o evento da sumula da partida

  Scenario: Impedir gerenciamento de sumula por usuario nao organizador
    Given que existe uma partida cadastrada
    And que o usuario autenticado nao e o organizador
    When ele tentar registrar eventos da partida
    Then o sistema deve impedir a operacao

  Scenario: Impedir registro de eventos para jogador nao pertencente ao time
    Given que existe uma partida cadastrada
    And que o jogador nao pertence aos times da partida
    When o organizador tentar registrar um evento para esse jogador
    Then o sistema deve impedir o registro
