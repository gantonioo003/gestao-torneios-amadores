# Mapeado para F5: Gerenciar times do usuário
Feature: Gerenciar time do usuário

  As a usuário autenticado
  I want criar e gerenciar meus times
  So that eu possa inscrevê-los em torneios

  Scenario: Usuário autenticado cria um novo time com dados válidos
    Given que o usuário está autenticado
    When ele cadastrar um novo time com nome válido
    Then o sistema deve registrar o time para esse usuário

  Scenario: Usuário edita o nome do time cadastrado
    Given que o usuário está autenticado
    And que ele é responsável por um time
    When ele alterar o nome do time
    Then o sistema deve atualizar os dados do time

  Scenario: Usuário exclui time sem vínculo em torneio
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time não está vinculado a nenhum torneio
    When ele solicitar a exclusão do time
    Then o sistema deve remover o time

  Scenario: Usuário tenta excluir time vinculado a torneio
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time está vinculado a um torneio
    When ele solicitar a exclusão do time
    Then o sistema deve impedir a exclusão

  Scenario: Usuário não autenticado tenta criar time
    Given que o usuário não está autenticado
    When ele tentar cadastrar um novo time
    Then o sistema deve exigir autenticação
