Feature: Vincular um time a um usuário responsável

  As a usuário autenticado
  I want vincular um time a um responsável
  So that o sistema saiba quem pode gerenciar a equipe

  Scenario: Vincular time a um usuário responsável com sucesso
    Given que o usuário está autenticado
    And que existe um time cadastrado
    When ele for definido como responsável pelo time
    Then o sistema deve vincular o time a esse usuário

  Scenario: Alterar o responsável de um time
    Given que o usuário está autenticado
    And que existe um time com responsável definido
    When for registrado um novo usuário responsável pelo time
    Then o sistema deve atualizar o vínculo de responsabilidade

  Scenario: Impedir vínculo de time a usuário inexistente
    Given que existe um time cadastrado
    When for informado um usuário inexistente como responsável
    Then o sistema deve impedir o vínculo

  Scenario: Impedir gerenciamento do time por usuário não vinculado
    Given que existe um time vinculado a um usuário responsável
    And que outro usuário autenticado não é o responsável pelo time
    When ele tentar gerenciar o time
    Then o sistema deve impedir a operação