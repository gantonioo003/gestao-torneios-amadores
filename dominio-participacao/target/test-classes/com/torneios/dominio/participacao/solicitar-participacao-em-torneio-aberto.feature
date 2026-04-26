Feature: Solicitar participação de um time em torneio aberto

  As a usuário autenticado
  I want solicitar a participação do meu time em um torneio aberto
  So that eu possa disputar a competição

  Scenario: Usuário autenticado solicita participação com time cadastrado
    Given que o usuário está autenticado
    And que ele possui um time cadastrado
    And que o torneio está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação do seu time no torneio
    Then o sistema deve registrar a solicitação de participação

  Scenario: Usuário sem time cadastrado tenta solicitar participação
    Given que o usuário está autenticado
    And que ele não possui time cadastrado
    And que o torneio está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação em um torneio
    Then o sistema deve impedir a solicitação
    And deve informar que é necessário possuir um time cadastrado

  Scenario: Usuário não autenticado tenta solicitar participação
    Given que o usuário não está autenticado
    And que o torneio está com vagas abertas para solicitação de participação
    When ele solicitar participação em um torneio
    Then o sistema deve exigir autenticação

  Scenario: Usuário tenta solicitar participação em torneio sem vagas abertas
    Given que o usuário está autenticado
    And que ele possui um time cadastrado
    And que o torneio não está com vagas abertas para solicitação de participação
    When o usuário solicitar a participação do seu time no torneio
    Then o sistema deve impedir a solicitação

  Scenario: Responsável tenta solicitar participação duplicada no mesmo torneio
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o torneio está com vagas abertas para solicitação de participação
    And que já existe uma solicitação pendente do time para esse torneio
    When o usuário solicitar novamente a participação do seu time no torneio
    Then o sistema deve impedir a solicitação duplicada

  Scenario: Responsável visualiza solicitações pendentes do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui solicitações de participação pendentes
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve exibir as solicitações pendentes do time

  Scenario: Responsável visualiza solicitações aprovadas do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui solicitações de participação aprovadas
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve exibir as solicitações aprovadas do time

  Scenario: Responsável visualiza solicitações rejeitadas do time
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time possui solicitações de participação rejeitadas
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve exibir as solicitações rejeitadas do time

  Scenario: Responsável acessa área de convites quando não há solicitações
    Given que o usuário está autenticado
    And que ele é responsável por um time
    And que o time não possui solicitações de participação
    When ele acessar a área de convites e solicitações do time
    Then o sistema deve informar que não há convites ou solicitações para o time

  Scenario: Usuário não responsável tenta visualizar convites do time
    Given que o usuário está autenticado
    And que ele não é responsável pelo time
    When ele tentar acessar os convites do time
    Then o sistema deve impedir a operação

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
