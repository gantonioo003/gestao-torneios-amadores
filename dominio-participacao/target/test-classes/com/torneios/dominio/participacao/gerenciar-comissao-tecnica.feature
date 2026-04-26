Feature: Gerenciar comissão técnica de um time

  As a usuário responsável por um time
  I want gerenciar a comissão técnica
  So that eu possa definir os responsáveis pela equipe

  Scenario: Associar técnico a um time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    When ele associar um técnico com dados válidos ao time
    Then o sistema deve registrar o técnico na comissão técnica do time

  Scenario: Editar dados de um técnico do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui técnico associado
    When ele alterar os dados do técnico
    Then o sistema deve atualizar os dados do técnico

  Scenario: Remover técnico da comissão técnica do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui técnico associado
    When ele solicitar a remoção do técnico
    Then o sistema deve remover o técnico da comissão técnica do time

  Scenario: Impedir gerenciamento da comissão técnica por usuário não responsável
    Given que o usuário está autenticado
    And que ele não é responsável pelo time
    When ele tentar associar um técnico ao time
    Then o sistema deve impedir a operação

  Scenario: Responsável tenta remover técnico quando não há técnico associado
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time não possui técnico associado
    When ele tentar remover o técnico do time
    Then o sistema deve informar que não existe técnico associado ao time
