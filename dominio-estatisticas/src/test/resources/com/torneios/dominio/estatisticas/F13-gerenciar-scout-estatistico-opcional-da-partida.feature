Feature: Gerenciar scout estatistico opcional da partida

  As a organizador do torneio
  I want registrar, corrigir e remover eventos individuais opcionais da partida
  So that o scout detalhado possa alimentar estatisticas, rankings e historico dos jogadores quando esse nivel de detalhe for desejado

  Scenario: Manter partida sem scout detalhado
    Given que existe uma partida cadastrada
    And que o usuario autenticado e o organizador
    When ele optar por nao registrar eventos individuais da partida
    Then o sistema deve manter o scout opcional vazio

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

  Scenario: Registrar substituicao mesmo quando a partida nao possui mesa tatica
    Given que existe uma partida cadastrada sem mesa tatica informada
    And que o usuario autenticado e o organizador
    When ele registrar uma substituicao trocando um jogador por outro
    Then o sistema deve armazenar a substituicao no scout da partida

  Scenario: Corrigir evento individual do scout
    Given que existe um evento individual registrado no scout da partida
    And que o usuario autenticado e o organizador
    When ele corrigir o evento individual do scout
    Then o sistema deve atualizar o evento estatistico da partida

  Scenario: Remover evento individual do scout
    Given que existe um evento individual registrado no scout da partida
    And que o usuario autenticado e o organizador
    When ele remover o evento individual do scout
    Then o sistema deve retirar o evento do scout da partida

  Scenario: Impedir gerenciamento do scout por usuario nao organizador
    Given que existe uma partida cadastrada
    And que o usuario autenticado nao e o organizador
    When ele tentar registrar eventos da partida
    Then o sistema deve impedir a operacao

  Scenario: Impedir registro de eventos para jogador nao pertencente ao time
    Given que existe uma partida cadastrada
    And que o jogador nao pertence aos times da partida
    When o organizador tentar registrar um evento para esse jogador
    Then o sistema deve impedir o registro
