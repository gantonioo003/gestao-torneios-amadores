Feature: Calcular e visualizar estatÃ­sticas do torneio

  As a usuÃ¡rio da plataforma
  I want visualizar estatÃ­sticas e notas dos jogadores
  So that eu possa acompanhar o desempenho individual e coletivo

  Scenario: Calcular nota estatÃ­stica do jogador
    Given que existem eventos registrados para um jogador
    When o sistema calcular a nota estatÃ­stica
    Then a nota deve refletir os eventos positivos e negativos

  Scenario: Gerar ranking de artilharia
    Given que existem gols registrados no torneio
    When o usuÃ¡rio acessar a artilharia
    Then o sistema deve exibir os jogadores ordenados por nÃºmero de gols

  Scenario: Visualizar estatÃ­sticas dos jogadores
    Given que existem estatÃ­sticas registradas no torneio
    When o usuÃ¡rio acessar as estatÃ­sticas
    Then o sistema deve exibir os dados dos jogadores

  Scenario: Atualizar estatÃ­sticas apÃ³s novos eventos
    Given que novos eventos foram registrados em uma partida
    When o usuÃ¡rio acessar as estatÃ­sticas
    Then o sistema deve exibir os dados atualizados
