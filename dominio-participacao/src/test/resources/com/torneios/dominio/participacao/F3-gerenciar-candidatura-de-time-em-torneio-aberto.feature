Feature: Gerenciar candidatura de time em torneio aberto

  As a usuario autenticado responsavel por um time
  I want gerenciar a candidatura do meu time em torneios abertos
  So that eu possa solicitar participacao, acompanhar o status e cancelar uma candidatura pendente

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
