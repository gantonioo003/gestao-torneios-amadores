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

  Scenario: Usuário não autenticado tenta criar time
    Given que o usuário não está autenticado
    When ele tentar cadastrar um novo time
    Then o sistema deve exigir autenticação

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
