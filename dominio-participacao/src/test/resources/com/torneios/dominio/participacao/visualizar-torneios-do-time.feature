# Relacionado a F1 e F5: visão dos torneios em que o time participa ou participou
Feature: Visualizar torneios do time

  As a usuário responsável por um time
  I want visualizar os torneios em que meu time está participando ou participou
  So that eu possa acompanhar o histórico de competições do time

  Scenario: Responsável visualiza torneios em que o time está vinculado
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time está vinculado a pelo menos um torneio
    When ele acessar a área de torneios do time
    Then o sistema deve exibir os torneios em que o time participa

  Scenario: Responsável acessa torneios quando o time não está em nenhum torneio
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time não está vinculado a nenhum torneio
    When ele acessar a área de torneios do time
    Then o sistema deve informar que o time não está vinculado a nenhum torneio

  Scenario: Usuário não autenticado tenta visualizar torneios do time
    Given que o usuário não está autenticado
    When ele tentar acessar os torneios do time
    Then o sistema deve exigir autenticação

  Scenario: Usuário não responsável tenta visualizar torneios do time
    Given que o usuário está autenticado
    And que ele não é responsável pelo time
    When ele tentar acessar os torneios do time
    Then o sistema deve impedir a operação
