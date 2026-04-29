Feature: Consolidar estatisticas e rankings do torneio

  As a usuario da plataforma
  I want que o sistema consolide notas, rankings e historico dos jogadores
  So that o desempenho do torneio seja atualizado automaticamente a partir da sumula

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
    Then o sistema deve manter apenas o placar oficial sem dados estatisticos
