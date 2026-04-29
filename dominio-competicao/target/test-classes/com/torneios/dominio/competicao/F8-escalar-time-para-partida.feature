Feature: Definir escalacao do time para uma partida

  As a responsavel pelo time ou tecnico associado
  I want definir o esquema tatico, os jogadores titulares por posicao e os reservas
  So that o time esteja escalado corretamente para a partida

  Scenario: Definir escalacao com esquema tatico, titulares por posicao e reservas com sucesso
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    And que o esquema tatico escolhido e compativel com o formato de equipe
    When ele definir a escalacao indicando os titulares por posicao e os reservas
    Then o sistema deve armazenar a escalacao para o time naquela partida

  Scenario: Permitir que o tecnico associado defina a escalacao
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o tecnico esta associado ao time
    When ele definir a escalacao do time para a partida
    Then o sistema deve armazenar a escalacao para o time naquela partida

  Scenario: Impedir escalacao por usuario que nao e responsavel nem tecnico do time
    Given que existe uma partida cadastrada no torneio
    And que o usuario autenticado nao e responsavel nem tecnico do time
    When ele tentar definir a escalacao do time para a partida
    Then o sistema deve impedir a operacao

  Scenario: Impedir escalacao com quantidade de titulares diferente do formato de equipe
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar definir a escalacao com quantidade de titulares diferente do formato de equipe
    Then o sistema deve impedir a operacao

  Scenario: Impedir escalacao com esquema tatico incompativel com o formato de equipe
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar definir a escalacao com um esquema tatico incompativel com o formato de equipe
    Then o sistema deve impedir a operacao

  Scenario: Impedir escalacao com jogador que nao pertence ao elenco do time
    Given que existe uma partida cadastrada no torneio
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar incluir na escalacao um jogador que nao pertence ao elenco do time
    Then o sistema deve impedir a operacao

  Scenario: Impedir o mesmo jogador como titular e reserva da mesma escalacao
    Given que existe uma partida cadastrada no torneio
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar incluir o mesmo jogador como titular e como reserva
    Then o sistema deve impedir a operacao

  Scenario: Editar escalacao enquanto a partida nao foi iniciada
    Given que existe uma escalacao definida para uma partida que ainda nao foi iniciada
    And que o usuario autenticado e o responsavel pelo time
    When ele alterar o esquema tatico ou os jogadores da escalacao
    Then o sistema deve atualizar a escalacao do time naquela partida

  Scenario: Impedir edicao de escalacao apos o inicio da partida
    Given que existe uma escalacao definida para uma partida que ja foi iniciada
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar alterar a escalacao do time para a partida
    Then o sistema deve impedir a operacao

  Scenario: Aceitar quantidade qualquer de reservas, inclusive zero
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele definir a escalacao sem incluir reservas
    Then o sistema deve armazenar a escalacao com lista de reservas vazia
