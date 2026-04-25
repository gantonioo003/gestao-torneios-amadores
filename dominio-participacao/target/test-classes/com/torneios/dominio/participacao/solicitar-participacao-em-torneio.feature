# Mapeado para F3: Solicitar participação de um time em torneio aberto
Feature: Solicitar participação de time em torneio

  As a usuário autenticado responsável por um time
  I want solicitar a participação do meu time em um torneio aberto
  So that eu possa inscrever meu time na competição

  Scenario: Responsável solicita participação com time cadastrado em torneio aberto
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o torneio está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação do seu time no torneio
    Then o sistema deve registrar a solicitação de participação como pendente

  Scenario: Usuário sem time cadastrado tenta solicitar participação
    Given que o usuário está autenticado
    And que ele não possui time cadastrado
    And que o torneio está com vagas abertas para solicitação de participação
    When o usuário tentar solicitar participação no torneio
    Then o sistema deve impedir a solicitação
    And deve informar que é necessário possuir um time cadastrado

  Scenario: Usuário não autenticado tenta solicitar participação
    Given que o usuário não está autenticado
    And que o torneio está com vagas abertas para solicitação de participação
    When ele tentar solicitar participação no torneio
    Then o sistema deve exigir autenticação

  Scenario: Responsável tenta solicitar participação em torneio sem vagas abertas
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o torneio não está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação do seu time no torneio
    Then o sistema deve impedir a solicitação

  Scenario: Responsável tenta solicitar participação duplicada no mesmo torneio
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o torneio está com vagas abertas para solicitação de participação
    And que já existe uma solicitação pendente do time para esse torneio
    When o usuário solicitar novamente a participação do seu time no torneio
    Then o sistema deve impedir a solicitação duplicada
