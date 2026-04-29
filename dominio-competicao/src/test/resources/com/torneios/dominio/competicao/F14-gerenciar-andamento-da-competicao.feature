Feature: Gerenciar andamento da competicao

  As a usuario da plataforma
  I want acompanhar o andamento da competicao atualizado apos cada resultado
  So that classificacao, chaveamento e status das partidas fiquem consistentes

  Scenario: Atualizar classificacao e status da partida apos resultado
    Given que um resultado de partida foi registrado
    When o sistema gerenciar o andamento apos o resultado
    Then o sistema deve atualizar a classificacao e marcar a partida como encerrada

  Scenario: Gerenciar chaveamento em torneio mata-mata
    Given que existe um torneio com formato mata-mata
    When o sistema gerenciar o chaveamento do torneio
    Then o sistema deve manter o chaveamento atualizado

  Scenario: Consultar classificacao em torneio de pontos corridos
    Given que existe um torneio com formato pontos corridos
    When o usuario consultar o andamento por classificacao
    Then o sistema deve exibir a tabela com a pontuacao dos times

  Scenario: Impedir gerenciamento de andamento sem estrutura gerada
    Given que o torneio ainda nao possui estrutura definida
    When o usuario consultar classificacao ou chaveamento
    Then o sistema deve informar que a competicao ainda nao foi iniciada
