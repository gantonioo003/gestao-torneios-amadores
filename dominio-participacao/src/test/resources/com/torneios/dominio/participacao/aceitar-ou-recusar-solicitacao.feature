# Mapeado para F4: Avaliar solicitações de participação de times no torneio
# Visão do responsável pelo time que RECEBE a resposta (convite/solicitacao)
Feature: Aceitar ou recusar solicitação de participação em torneio

  As a usuário responsável por um time
  I want aceitar ou recusar solicitações de participação em torneios
  So that eu possa decidir em quais competições meu time irá participar

  Scenario: Responsável aceita solicitação de participação aprovada pelo organizador
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que existe uma solicitação de participação aprovada pelo organizador para o time
    When o responsável aceitar a participação no torneio
    Then o sistema deve vincular o time ao torneio

  Scenario: Responsável recusa solicitação de participação aprovada pelo organizador
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que existe uma solicitação de participação aprovada pelo organizador para o time
    When o responsável recusar a participação no torneio
    Then o sistema deve registrar a recusa e não vincular o time ao torneio

  Scenario: Usuário não responsável tenta aceitar solicitação do time
    Given que o usuário está autenticado
    And que ele não é responsável pelo time
    And que existe uma solicitação de participação aprovada pelo organizador para o time
    When ele tentar aceitar a solicitação de participação
    Then o sistema deve impedir a operação

  Scenario: Responsável tenta aceitar solicitação inexistente
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que não existe solicitação de participação para o time
    When o responsável tentar aceitar a participação
    Then o sistema deve informar que não há solicitação para aceitar
