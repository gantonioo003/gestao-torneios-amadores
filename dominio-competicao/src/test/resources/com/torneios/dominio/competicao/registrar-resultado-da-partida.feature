Feature: Registrar resultado da partida

  As a organizador do torneio
  I want registrar o resultado de uma partida
  So that o sistema atualize o andamento da competiÃ§Ã£o

  Scenario: Registrar resultado vÃ¡lido de uma partida
    Given que existe uma partida cadastrada no torneio
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When ele registrar o placar da partida
    Then o sistema deve armazenar o resultado da partida

  Scenario: Atualizar classificaÃ§Ã£o apÃ³s registro de resultado
    Given que existe uma partida cadastrada no torneio
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When ele registrar o resultado da partida
    Then o sistema deve atualizar automaticamente a classificaÃ§Ã£o ou chaveamento

  Scenario: Impedir registro de resultado por usuÃ¡rio nÃ£o organizador
    Given que existe uma partida cadastrada no torneio
    And que o usuÃ¡rio autenticado nÃ£o Ã© o organizador
    When ele tentar registrar o resultado da partida
    Then o sistema deve impedir a operaÃ§Ã£o

  Scenario: Impedir registro de resultado para partida inexistente
    Given que nÃ£o existe a partida informada
    When o usuÃ¡rio tentar registrar um resultado
    Then o sistema deve impedir a operaÃ§Ã£o
