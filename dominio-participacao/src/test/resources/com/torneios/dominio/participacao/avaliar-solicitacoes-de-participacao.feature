Feature: Avaliar solicitaÃ§Ãµes de participaÃ§Ã£o de times no torneio

  As a organizador do torneio
  I want avaliar solicitaÃ§Ãµes de participaÃ§Ã£o de times
  So that eu possa definir quais equipes entrarÃ£o na competiÃ§Ã£o

  Scenario: Organizador aprova solicitaÃ§Ã£o de participaÃ§Ã£o
    Given que existe uma solicitaÃ§Ã£o pendente de participaÃ§Ã£o para um torneio
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When o organizador aprovar a solicitaÃ§Ã£o
    Then o sistema deve registrar o time como participante aprovado do torneio

  Scenario: Organizador rejeita solicitaÃ§Ã£o de participaÃ§Ã£o
    Given que existe uma solicitaÃ§Ã£o pendente de participaÃ§Ã£o para um torneio
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When o organizador rejeitar a solicitaÃ§Ã£o
    Then o sistema deve registrar a solicitaÃ§Ã£o como rejeitada

  Scenario: UsuÃ¡rio que nÃ£o Ã© organizador tenta avaliar solicitaÃ§Ã£o
    Given que existe uma solicitaÃ§Ã£o pendente de participaÃ§Ã£o para um torneio
    And que o usuÃ¡rio autenticado nÃ£o Ã© o organizador do torneio
    When ele tentar aprovar a solicitaÃ§Ã£o
    Then o sistema deve impedir a operaÃ§Ã£o

  Scenario: Organizador tenta avaliar solicitaÃ§Ã£o inexistente
    Given que nÃ£o existe solicitaÃ§Ã£o pendente para o torneio
    And que o usuÃ¡rio autenticado Ã© o organizador do torneio
    When ele tentar avaliar uma solicitaÃ§Ã£o
    Then o sistema deve informar que nÃ£o hÃ¡ solicitaÃ§Ã£o pendente para avaliaÃ§Ã£o
