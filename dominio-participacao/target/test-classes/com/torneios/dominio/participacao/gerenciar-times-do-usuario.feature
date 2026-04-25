Feature: Gerenciar times do usuário

  As a usuário autenticado
  I want gerenciar meus times
  So that eu possa utilizá-los em torneios

  Scenario: Criar um novo time com sucesso
    Given que o usuário está autenticado
    When ele cadastrar um novo time com informações válidas
    Then o sistema deve registrar o time para esse usuário

  Scenario: Editar informações de um time do usuário
    Given que o usuário está autenticado
    And que ele possui um time cadastrado
    When ele alterar as informações do time
    Then o sistema deve atualizar os dados do time

  Scenario: Excluir um time do usuário sem vínculo em torneio
    Given que o usuário está autenticado
    And que ele possui um time cadastrado
    And que o time não está vinculado a nenhum torneio
    When ele solicitar a exclusão do time
    Then o sistema deve remover o time

  Scenario: Impedir exclusão de time vinculado a torneio
    Given que o usuário está autenticado
    And que ele possui um time cadastrado
    And que o time está vinculado a um torneio
    When ele solicitar a exclusão do time
    Then o sistema deve impedir a exclusão