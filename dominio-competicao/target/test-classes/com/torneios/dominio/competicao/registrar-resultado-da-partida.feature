Feature: Registrar resultado da partida

  As a organizador do torneio
  I want registrar o resultado de uma partida
  So that o sistema atualize o andamento da competição

  Scenario: Registrar apenas o placar oficial da partida sem informar eventos estatisticos
    Given que existe uma partida cadastrada no torneio
    And que o usuário autenticado é o organizador do torneio
    When ele registrar o placar da partida
    Then o sistema deve armazenar o resultado da partida

  Scenario: Atualizar classificação após registro de resultado
    Given que existe uma partida cadastrada no torneio
    And que o usuário autenticado é o organizador do torneio
    When ele registrar o resultado da partida
    Then o sistema deve atualizar automaticamente a classificação ou chaveamento

  Scenario: Impedir registro de resultado por usuário não organizador
    Given que existe uma partida cadastrada no torneio
    And que o usuário autenticado não é o organizador
    When ele tentar registrar o resultado da partida
    Then o sistema deve impedir a operação

  Scenario: Impedir registro de resultado para partida inexistente
    Given que não existe a partida informada
    When o usuário tentar registrar um resultado
    Then o sistema deve impedir a operação
