Feature: Gerenciar inscricoes e participantes do torneio

  As a organizador do torneio
  I want avaliar solicitacoes e ajustar a lista final de participantes
  So that apenas os times aprovados disputem a competicao antes do inicio

  Scenario: Aprovar solicitacao e incluir time na lista final
    Given que existe uma solicitacao pendente de participacao para um torneio
    And que o usuario autenticado e o organizador do torneio
    When o organizador aprovar a solicitacao
    Then o sistema deve registrar o time como participante aprovado do torneio

  Scenario: Rejeitar solicitacao de participacao
    Given que existe uma solicitacao pendente de participacao para um torneio
    And que o usuario autenticado e o organizador do torneio
    When o organizador rejeitar a solicitacao
    Then o sistema deve registrar a solicitacao como rejeitada

  Scenario: Remover time aprovado antes do inicio do torneio
    Given que existe um time aprovado na lista final de participantes
    And que o torneio ainda nao foi iniciado
    And que o usuario autenticado e o organizador do torneio
    When o organizador remover o time da lista final
    Then o sistema deve retirar o time da lista final do torneio

  Scenario: Impedir gerenciamento por usuario que nao e organizador
    Given que existe uma solicitacao pendente de participacao para um torneio
    And que o usuario autenticado nao e o organizador do torneio
    When ele tentar aprovar a solicitacao
    Then o sistema deve impedir a operacao

  Scenario: Impedir alteracao da lista final apos inicio do torneio
    Given que existe um time aprovado na lista final de participantes
    And que o torneio ja foi iniciado
    And que o usuario autenticado e o organizador do torneio
    When ele tentar alterar a lista final de participantes
    Then o sistema deve impedir a operacao

  Scenario: Organizador visualiza lista de times candidatos pendentes
    Given que o usuario autenticado e o organizador do torneio
    And que existem solicitacoes pendentes de times para o torneio
    When o organizador acessar a lista de candidatos
    Then o sistema deve exibir os times com solicitacoes pendentes

  Scenario: Organizador tenta avaliar solicitacao inexistente
    Given que nao existe solicitacao pendente para o torneio
    And que o usuario autenticado e o organizador do torneio
    When ele tentar avaliar uma solicitacao
    Then o sistema deve informar que nao ha solicitacao pendente para avaliacao
