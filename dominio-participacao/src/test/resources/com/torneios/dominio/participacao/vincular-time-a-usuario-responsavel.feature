Feature: Vincular um time a um usuÃ¡rio responsÃ¡vel

  As a usuÃ¡rio autenticado
  I want vincular um time a um responsÃ¡vel
  So that o sistema saiba quem pode gerenciar a equipe

  Scenario: Vincular time a um usuÃ¡rio responsÃ¡vel com sucesso
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que existe um time cadastrado
    When ele for definido como responsÃ¡vel pelo time
    Then o sistema deve vincular o time a esse usuÃ¡rio

  Scenario: Alterar o responsÃ¡vel de um time
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que existe um time com responsÃ¡vel definido
    When for registrado um novo usuÃ¡rio responsÃ¡vel pelo time
    Then o sistema deve atualizar o vÃ­nculo de responsabilidade

  Scenario: Impedir vÃ­nculo de time a usuÃ¡rio inexistente
    Given que existe um time cadastrado
    When for informado um usuÃ¡rio inexistente como responsÃ¡vel
    Then o sistema deve impedir o vÃ­nculo

  Scenario: Impedir gerenciamento do time por usuÃ¡rio nÃ£o vinculado
    Given que existe um time vinculado a um usuÃ¡rio responsÃ¡vel
    And que outro usuÃ¡rio autenticado nÃ£o Ã© o responsÃ¡vel pelo time
    When ele tentar gerenciar o time
    Then o sistema deve impedir a operaÃ§Ã£o
