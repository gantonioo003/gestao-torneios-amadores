# Mapeado para F4: Avaliar solicitações de participação de times no torneio
# Visão do organizador que avalia os times candidatos
Feature: Avaliar times candidatos à participação no torneio

  As a organizador do torneio
  I want avaliar os times que solicitaram participação
  So that eu possa selecionar os participantes da competição

  Scenario: Organizador aprova time candidato com solicitação pendente
    Given que o usuário autenticado é o organizador do torneio
    And que existe uma solicitação pendente de um time para o torneio
    When o organizador aprovar o time candidato
    Then o sistema deve registrar a solicitação como aprovada

  Scenario: Organizador rejeita time candidato com solicitação pendente
    Given que o usuário autenticado é o organizador do torneio
    And que existe uma solicitação pendente de um time para o torneio
    When o organizador rejeitar o time candidato
    Then o sistema deve registrar a solicitação como rejeitada

  Scenario: Organizador visualiza lista de times candidatos pendentes
    Given que o usuário autenticado é o organizador do torneio
    And que existem solicitações pendentes de times para o torneio
    When o organizador acessar a lista de candidatos
    Then o sistema deve exibir os times com solicitações pendentes

  Scenario: Organizador tenta avaliar time quando não há candidatos pendentes
    Given que o usuário autenticado é o organizador do torneio
    And que não existem solicitações pendentes para o torneio
    When o organizador tentar acessar a lista de candidatos
    Then o sistema deve informar que não há candidatos pendentes

  Scenario: Usuário que não é organizador tenta avaliar time candidato
    Given que o usuário está autenticado
    And que ele não é organizador do torneio
    And que existe uma solicitação pendente de um time para o torneio
    When ele tentar aprovar o time candidato
    Then o sistema deve impedir a operação
