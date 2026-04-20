Feature: Gerenciar comissÃ£o tÃ©cnica de um time

  As a usuÃ¡rio responsÃ¡vel por um time
  I want gerenciar a comissÃ£o tÃ©cnica
  So that eu possa definir os responsÃ¡veis pela equipe

  Scenario: Associar tÃ©cnico a um time
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele Ã© responsÃ¡vel por um time
    When ele associar um tÃ©cnico com dados vÃ¡lidos ao time
    Then o sistema deve registrar o tÃ©cnico na comissÃ£o tÃ©cnica do time

  Scenario: Editar dados de um tÃ©cnico do time
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele Ã© responsÃ¡vel por um time
    And que o time possui tÃ©cnico associado
    When ele alterar os dados do tÃ©cnico
    Then o sistema deve atualizar os dados do tÃ©cnico

  Scenario: Remover tÃ©cnico da comissÃ£o tÃ©cnica do time
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele Ã© responsÃ¡vel por um time
    And que o time possui tÃ©cnico associado
    When ele solicitar a remoÃ§Ã£o do tÃ©cnico
    Then o sistema deve remover o tÃ©cnico da comissÃ£o tÃ©cnica do time

  Scenario: Impedir gerenciamento da comissÃ£o tÃ©cnica por usuÃ¡rio nÃ£o responsÃ¡vel
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele nÃ£o Ã© responsÃ¡vel pelo time
    When ele tentar associar um tÃ©cnico ao time
    Then o sistema deve impedir a operaÃ§Ã£o
