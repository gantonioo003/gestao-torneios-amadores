# Relacionado a F4 e F3: visão do responsável sobre o status das suas solicitações
Feature: Visualizar convites e solicitações do time

  As a usuário responsável por um time
  I want visualizar os convites e o status das solicitações de participação do meu time
  So that eu possa acompanhar em quais torneios meu time foi aceito ou rejeitado

  Scenario: Responsável visualiza solicitações pendentes do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui solicitações de participação pendentes
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve exibir as solicitações pendentes do time

  Scenario: Responsável visualiza solicitações aprovadas do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui solicitações de participação aprovadas
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve exibir as solicitações aprovadas do time

  Scenario: Responsável visualiza solicitações rejeitadas do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui solicitações de participação rejeitadas
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve exibir as solicitações rejeitadas do time

  Scenario: Responsável acessa área de convites quando não há solicitações
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time não possui solicitações de participação
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve informar que não há convites ou solicitações para o time

  Scenario: Usuário não responsável tenta visualizar convites do time
    Given que o usuário está autenticado
    And que ele não é responsável pelo time
    When ele tentar acessar os convites do time
    Then o sistema deve impedir a operação
