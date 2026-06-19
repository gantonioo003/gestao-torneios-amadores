Feature: Gerenciar preparacao e visualizacao da escalacao do time para uma partida

  As a tecnico responsavel pelo time
  I want informar titulares e reservas em lista ou mesa tatica antes da partida
  So that a escalacao seja preparada com privacidade e divulgada de forma coerente quando o jogo iniciar

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

  Scenario: Permitir informar apenas os titulares em lista
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele informar a escalacao somente com os titulares em lista
    Then o sistema deve salvar a escalacao em lista sem exigir esquema tatico

  Scenario: Manter a escalacao privada antes do inicio da partida
    Given que existe uma escalacao definida para uma partida que ainda nao foi iniciada
    When um visitante tentar consultar as escalacoes publicas da partida
    Then o sistema deve impedir a consulta publica da escalacao

  Scenario: Divulgar a escalacao quando a partida iniciar
    Given que existe uma escalacao definida para uma partida que ja foi iniciada
    When um visitante consultar as escalacoes publicas da partida
    Then o sistema deve divulgar a escalacao congelada

  Scenario: Adaptar o espacamento da mesa a quantidade de jogadores de cada linha
    Given que existe uma partida cadastrada no torneio com formato de equipe definido
    And que o usuario autenticado e o responsavel pelo time
    When ele gerar a escalacao em mesa tatica indicando os titulares por posicao e os reservas
    Then os jogadores de cada linha devem ficar distribuidos sem sobreposicao
