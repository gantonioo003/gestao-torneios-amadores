Feature: Calcular e visualizar estatísticas do torneio

  As a usuário da plataforma
  I want visualizar estatísticas e notas dos jogadores
  So that eu possa acompanhar o desempenho individual e coletivo

  Scenario: Calcular nota estatística do jogador
    Given que existem eventos registrados para um jogador
    When o sistema calcular a nota estatística
    Then a nota deve refletir os eventos positivos e negativos

  Scenario: Gerar ranking de artilharia
    Given que existem gols registrados no torneio
    When o usuário acessar a artilharia
    Then o sistema deve exibir os jogadores ordenados por número de gols

  Scenario: Visualizar estatísticas dos jogadores
    Given que existem estatísticas registradas no torneio
    When o usuário acessar as estatísticas
    Then o sistema deve exibir os dados dos jogadores

  Scenario: Atualizar estatísticas após novos eventos
    Given que novos eventos foram registrados em uma partida
    When o usuário acessar as estatísticas
    Then o sistema deve exibir os dados atualizados

  Scenario: Nao exibir estatisticas quando nao houver eventos registrados
    Given que existe uma partida sem eventos estatisticos registrados
    When o usuario acessar as estatisticas sem eventos
    Then o sistema deve manter apenas o placar oficial sem dados estatisticos
