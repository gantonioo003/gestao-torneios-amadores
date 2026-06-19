Feature: Gerenciar a consolidacao historica das estatisticas e rankings do torneio

  As a usuario da plataforma
  I want que o sistema consolide notas, rankings e historico dos jogadores
  So that o desempenho do torneio seja atualizado automaticamente a partir do scout detalhado quando ele existir

  Scenario: Consolidar notas, artilharia, assistencias e historico dos jogadores
    Given que existem eventos registrados para um jogador
    When o sistema consolidar estatisticas e rankings do torneio
    Then o sistema deve atualizar nota, artilharia, lideres de assistencias e historico do jogador

  Scenario: Gerar ranking de artilharia
    Given que existem gols registrados no torneio
    When o sistema consolidar a artilharia do torneio
    Then o sistema deve exibir os jogadores ordenados por numero de gols

  Scenario: Atualizar estatisticas apos novos eventos
    Given que novos eventos foram registrados em uma partida
    When o sistema consolidar estatisticas e rankings do torneio
    Then o sistema deve exibir os dados atualizados

  Scenario: Nao consolidar estatisticas detalhadas quando nao houver eventos registrados
    Given que existe uma partida sem eventos estatisticos registrados
    When o sistema tentar consolidar estatisticas sem eventos
    Then o sistema deve manter apenas o placar oficial sem scout detalhado

  Scenario: Ordenar lideres de assistencias e melhores notas
    Given que existem jogadores com assistencias e notas diferentes
    When o sistema ordenar os rankings individuais do torneio
    Then o lider de assistencias e o jogador de maior nota devem aparecer primeiro
