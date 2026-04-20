Feature: Gerenciar participantes aprovados do torneio

  As a organizador do torneio
  I want gerenciar os participantes aprovados
  So that eu possa definir quem disputará a competição

  Scenario: Aprovar time para participar do torneio
    Given que existe um torneio com vagas abertas
    And que existe uma solicitação pendente de participação
    And que o usuário autenticado é o organizador do torneio
    When ele aprovar a solicitação do time
    Then o sistema deve incluir o time entre os participantes aprovados

  Scenario: Remover time aprovado antes do início do torneio
    Given que existe um torneio ainda não iniciado
    And que existe um time aprovado no torneio
    And que o usuário autenticado é o organizador do torneio
    When ele remover o time da lista de participantes aprovados
    Then o sistema deve retirar o time do torneio

  Scenario: Impedir gerenciamento de participantes por usuário não organizador
    Given que existe um torneio
    And que o usuário autenticado não é o organizador do torneio
    When ele tentar aprovar ou remover participantes
    Then o sistema deve impedir a operação

  Scenario: Impedir alteração de participantes após início do torneio
    Given que existe um torneio já iniciado
    And que o usuário autenticado é o organizador do torneio
    When ele tentar alterar a lista de participantes aprovados
    Then o sistema deve impedir a operação