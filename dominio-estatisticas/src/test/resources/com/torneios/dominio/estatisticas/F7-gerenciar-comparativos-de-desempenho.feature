Feature: Gerenciar comparativos de desempenho entre times e jogadores

  As a usuario da plataforma
  I want gerar, salvar, consultar, atualizar e excluir comparativos de desempenho
  So that eu possa acompanhar analises escolhidas de times e jogadores

  Scenario: Gerar comparativo temporario entre jogadores
    Given que existem estatisticas registradas para dois jogadores
    When o usuario gerar um comparativo entre os jogadores
    Then o sistema deve exibir o comparativo temporario dos jogadores
    And o comparativo nao deve estar salvo ainda
    And deve indicar vantagem por estatisticas historico e ranking

  Scenario: Gerar comparativo temporario entre times
    Given que existem estatisticas registradas para dois times
    When o usuario gerar um comparativo entre os times
    Then o sistema deve exibir o comparativo temporario dos times
    And o comparativo nao deve estar salvo ainda
    And deve comparar gols assistencias cartoes historico e ranking

  Scenario: Salvar comparativo escolhido
    Given que existe um comparativo temporario gerado
    When o usuario salvar o comparativo escolhido
    Then o sistema deve armazenar o comparativo salvo

  Scenario: Consultar comparativos salvos
    Given que existe um comparativo salvo para o torneio
    When o usuario consultar os comparativos salvos do torneio
    Then o sistema deve listar os comparativos salvos

  Scenario: Atualizar comparativo salvo apos mudanca nos dados
    Given que existe um comparativo salvo para o torneio
    And que novos eventos alteraram o desempenho dos jogadores
    When o usuario atualizar o comparativo salvo
    Then o sistema deve substituir o comparativo pelos dados atualizados

  Scenario: Excluir comparativo salvo
    Given que existe um comparativo salvo para o torneio
    When o usuario excluir o comparativo salvo
    Then o sistema deve remover o comparativo do historico

  Scenario: Impedir comparativo sem dados estatisticos
    Given que nao existem estatisticas registradas para comparacao
    When o usuario tentar gerar um comparativo entre os jogadores
    Then o sistema deve impedir a comparacao de desempenho
