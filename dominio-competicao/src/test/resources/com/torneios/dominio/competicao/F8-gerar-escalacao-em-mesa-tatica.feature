Feature: Gerenciar visualizacao da escalacao do time em mesa tatica para uma partida

  As a responsavel pelo time ou tecnico associado
  I want gerar uma visualizacao da escalacao em uma mesa tatica com esquema, titulares por posicao e reservas
  So that a partida tenha uma representacao visual organizada do time em campo

  Scenario: Permitir partida seguir normalmente sem mesa tatica
    Given que existe uma partida cadastrada no torneio sem exigencia de escalacao
    When o sistema congelar as escalacoes antes do inicio
    Then a partida deve seguir sem escalacao cadastrada

  Scenario: Permitir que apenas um time gere mesa tatica sem bloquear a partida
    Given que existe uma partida cadastrada no torneio sem exigencia de escalacao
    And que apenas um time informou a escalacao
    When o sistema congelar as escalacoes antes do inicio
    Then o sistema deve manter apenas a mesa tatica informada sem bloquear a partida

  Scenario: Gerar mesa tatica com esquema tatico, titulares por posicao e reservas com sucesso
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    And que o esquema tatico escolhido e compativel com o formato de equipe
    When ele gerar a escalacao em mesa tatica indicando os titulares por posicao e os reservas
    Then o sistema deve gerar a mesa tatica do time para aquela partida
    And deve posicionar os titulares em campo conforme o esquema tatico

  Scenario: Permitir que o tecnico associado gere a mesa tatica
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o tecnico esta associado ao time
    When ele gerar a escalacao em mesa tatica do time para a partida
    Then o sistema deve gerar a mesa tatica do time para aquela partida

  Scenario: Impedir geracao de mesa tatica por usuario que nao e responsavel nem tecnico do time
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado nao e responsavel nem tecnico do time
    When ele tentar gerar a escalacao em mesa tatica do time para a partida
    Then o sistema deve impedir a operacao

  Scenario: Impedir geracao de mesa tatica com quantidade de titulares diferente do formato de equipe
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar gerar a escalacao em mesa tatica com quantidade de titulares diferente do formato de equipe
    Then o sistema deve impedir a operacao

  Scenario: Impedir geracao de mesa tatica com esquema tatico incompativel com o formato de equipe
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar gerar a escalacao em mesa tatica com um esquema tatico incompativel com o formato de equipe
    Then o sistema deve impedir a operacao

  Scenario: Impedir geracao de mesa tatica com jogador que nao pertence ao elenco do time
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar incluir na mesa tatica um jogador que nao pertence ao elenco do time
    Then o sistema deve impedir a operacao

  Scenario: Impedir o mesmo jogador como titular e reserva da mesma mesa tatica
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele tentar incluir o mesmo jogador como titular e como reserva na mesa tatica
    Then o sistema deve impedir a operacao

  Scenario: Editar mesa tatica enquanto a partida nao foi iniciada
    Given que existe uma escalacao definida para uma partida que ainda nao foi iniciada
    When ele alterar o esquema tatico ou os jogadores da mesa tatica
    Then o sistema deve atualizar a mesa tatica do time naquela partida

  Scenario: Impedir edicao de mesa tatica apos o inicio da partida
    Given que existe uma escalacao definida para uma partida que ja foi iniciada
    When ele tentar alterar a mesa tatica do time para a partida
    Then o sistema deve impedir a operacao

  Scenario: Aceitar quantidade qualquer de reservas, inclusive zero
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele gerar a escalacao em mesa tatica sem incluir reservas
    Then o sistema deve gerar a mesa tatica com lista de reservas vazia
