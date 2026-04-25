# Mapeado para F7: Gerenciar comissão técnica de um time
Feature: Gerenciar técnico do time

  As a usuário responsável por um time
  I want gerenciar o técnico associado ao time
  So that eu possa definir a comissão técnica da equipe

  Scenario: Responsável associa técnico ao time com dados válidos
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time não possui técnico associado
    When ele associar um técnico com dados válidos ao time
    Then o sistema deve registrar o técnico na comissão técnica do time

  Scenario: Responsável edita dados do técnico associado
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui técnico associado
    When ele alterar o nome do técnico
    Then o sistema deve atualizar os dados do técnico

  Scenario: Responsável remove técnico da comissão técnica
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui técnico associado
    When ele solicitar a remoção do técnico
    Then o sistema deve remover o técnico da comissão técnica do time

  Scenario: Usuário não responsável tenta gerenciar técnico do time
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
