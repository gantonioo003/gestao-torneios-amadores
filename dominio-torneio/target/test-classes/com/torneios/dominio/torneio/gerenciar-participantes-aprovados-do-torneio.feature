Feature: Gerenciar participantes aprovados do torneio

  As a organizador do torneio
  I want gerenciar os participantes aprovados
  So that eu possa definir quem disputarÃ¡ a competiÃ§Ã£o

  Scenario: Aprovar time para participar do torneio
    Given que existe um torneio com vagas abertas
    And que existe uma solicitaÃ§Ã£o pendente de participaÃ§Ã£o
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When ele aprovar a solicitaÃ§Ã£o do time
    Then o sistema deve incluir o time entre os participantes aprovados

  Scenario: Remover time aprovado antes do inÃ­cio do torneio
    Given que existe um torneio ainda nÃ£o iniciado
    And que existe um time aprovado no torneio
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When ele remover o time da lista de participantes aprovados
    Then o sistema deve retirar o time do torneio

  Scenario: Impedir gerenciamento de participantes por usuÃ¡rio nÃ£o organizador
    Given que existe um torneio
    And que o usuÃ¡rio autenticado nÃ£o Ã© o organizador do torneio
    When ele tentar aprovar ou remover participantes
    Then o sistema deve impedir a operaÃ§Ã£o

  Scenario: Impedir alteraÃ§Ã£o de participantes apÃ³s inÃ­cio do torneio
    Given que existe um torneio jÃ¡ iniciado
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When ele tentar alterar a lista de participantes aprovados
    Then o sistema deve impedir a operaÃ§Ã£o
