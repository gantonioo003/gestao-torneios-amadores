Feature: Avaliar solicitações de participação de times

  As a organizador do torneio
  I want avaliar solicitações de participação de times
  So that eu possa definir quais equipes entrarão na competição

  Scenario: Organizador aprova solicitação de participação
    Given que existe uma solicitação pendente de participação para um torneio
    And que o usuário autenticado é o organizador do torneio
    When o organizador aprovar a solicitação
    Then o sistema deve registrar o time como participante aprovado do torneio

  Scenario: Organizador rejeita solicitação de participação
    Given que existe uma solicitação pendente de participação para um torneio
    And que o usuário autenticado é o organizador do torneio
    When o organizador rejeitar a solicitação
    Then o sistema deve registrar a solicitação como rejeitada

  Scenario: Usuário que não é organizador tenta avaliar solicitação
    Given que existe uma solicitação pendente de participação para um torneio
    And que o usuário autenticado não é o organizador do torneio
    When ele tentar aprovar a solicitação
    Then o sistema deve impedir a operação

  Scenario: Organizador tenta avaliar solicitação inexistente
    Given que não existe solicitação pendente para o torneio
    And que o usuário autenticado é o organizador do torneio
    When ele tentar avaliar uma solicitação
    Then o sistema deve informar que não há solicitação pendente para avaliação

  Scenario: Organizador visualiza lista de times candidatos pendentes
    Given que o usuário autenticado é o organizador do torneio
    And que existem solicitações pendentes de times para o torneio
    When o organizador acessar a lista de candidatos
    Then o sistema deve exibir os times com solicitações pendentes
