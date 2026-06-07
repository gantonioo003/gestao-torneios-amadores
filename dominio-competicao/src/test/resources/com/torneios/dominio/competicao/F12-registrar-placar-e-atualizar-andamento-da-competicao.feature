Feature: Registrar placar oficial da partida e atualizar andamento da competicao

  As a organizador do torneio
  I want registrar o placar oficial de uma partida
  So that o sistema atualize automaticamente o andamento da competicao

  Scenario: Registrar apenas o placar oficial da partida sem informar eventos estatisticos
    Given que existe uma partida cadastrada no torneio
    And que o usuario autenticado e o organizador do torneio
    When ele registrar o placar da partida
    Then o sistema deve armazenar o resultado da partida

  Scenario: Atualizar classificacao apos registro de resultado
    Given que existe uma partida cadastrada no torneio
    And que o usuario autenticado e o organizador do torneio
    When ele registrar o resultado da partida
    Then o sistema deve atualizar automaticamente a classificacao ou chaveamento

  Scenario: Impedir registro de resultado por usuario nao organizador
    Given que existe uma partida cadastrada no torneio
    And que o usuario autenticado nao e o organizador
    When ele tentar registrar o resultado da partida
    Then o sistema deve impedir a operacao

  Scenario: Impedir registro de resultado para partida inexistente
    Given que nao existe a partida informada
    When o usuario tentar registrar um resultado
    Then o sistema deve impedir a operacao

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
