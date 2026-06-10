Feature: Gerenciar inscricoes e participantes do torneio

  As a usuario responsavel por time ou organizador de torneio
  I want gerenciar o fluxo completo de entrada dos times em torneios abertos
  So that candidaturas, avaliacoes e lista final de participantes fiquem controladas antes do inicio

  Scenario: Enviar candidatura com time cadastrado
    Given que o usuario esta autenticado
    And que ele possui um time cadastrado
    And que o torneio esta com vagas abertas para solicitacao de participacao
    When o usuario solicitar a participacao do seu time no torneio
    Then o sistema deve registrar a candidatura como pendente

  Scenario: Acompanhar status das candidaturas do time
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que ja existe uma solicitacao pendente do time para esse torneio
    When o usuario acompanhar suas candidaturas
    Then o sistema deve exibir o status das candidaturas do time

  Scenario: Cancelar candidatura pendente
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que existe uma candidatura pendente do time
    When o usuario cancelar a candidatura pendente
    Then o sistema deve marcar a candidatura como cancelada

  Scenario: Impedir cancelamento de candidatura ja avaliada
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que existe uma candidatura ja avaliada do time
    When o usuario tentar cancelar uma candidatura ja avaliada
    Then o sistema deve impedir o cancelamento da candidatura

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

  Scenario: Impedir candidatura sem time cadastrado
    Given que o usuario esta autenticado
    And que ele nao possui time cadastrado
    And que o torneio esta com vagas abertas para solicitacao de participacao
    When o usuario solicitar a participacao em um torneio
    Then o sistema deve impedir a solicitacao
    And deve informar que e necessario possuir um time cadastrado

  Scenario: Impedir candidatura de usuario nao autenticado
    Given que o usuario nao esta autenticado
    And que o torneio esta com vagas abertas para solicitacao de participacao
    When ele solicitar participacao em um torneio
    Then o sistema deve exigir autenticacao

  Scenario: Impedir candidatura em torneio fechado
    Given que o usuario esta autenticado
    And que ele possui um time cadastrado
    And que o torneio nao esta com vagas abertas para solicitacao de participacao
    When o usuario solicitar a participacao do seu time no torneio
    Then o sistema deve impedir a solicitacao

  Scenario: Impedir candidatura duplicada no mesmo torneio
    Given que o usuario esta autenticado
    And que ele e responsavel por um time
    And que o torneio esta com vagas abertas para solicitacao de participacao
    And que ja existe uma solicitacao pendente do time para esse torneio
    When o usuario solicitar novamente a participacao do seu time no torneio
    Then o sistema deve impedir a candidatura duplicada

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
