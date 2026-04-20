Feature: Solicitar participação de um time em torneio aberto

  As a usuário autenticado
  I want solicitar a participação do meu time em um torneio aberto
  So that eu possa disputar a competição

  Scenario: Usuário autenticado solicita participação com time cadastrado
    Given que o usuário está autenticado
    And que ele possui um time cadastrado
    And que o torneio está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação do seu time no torneio
    Then o sistema deve registrar a solicitação de participação

  Scenario: Usuário sem time cadastrado tenta solicitar participação
    Given que o usuário está autenticado
    And que ele não possui time cadastrado
    And que o torneio está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação em um torneio
    Then o sistema deve impedir a solicitação
    And deve informar que é necessário possuir um time cadastrado

  Scenario: Usuário não autenticado tenta solicitar participação
    Given que o usuário não está autenticado
    And que o torneio está com vagas abertas para solicitação de participação
    When ele solicitar participação em um torneio
    Then o sistema deve exigir autenticação

  Scenario: Usuário tenta solicitar participação em torneio sem vagas abertas
    Given que o usuário está autenticado
    And que ele possui um time cadastrado
    And que o torneio não está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação do seu time no torneio
    Then o sistema deve impedir a solicitação