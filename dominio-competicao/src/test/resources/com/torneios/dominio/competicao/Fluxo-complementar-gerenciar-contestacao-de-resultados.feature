Feature: Gerenciar auditoria e contestacao de resultados oficiais da partida

  As a responsavel por time participante
  I want contestar resultados oficiais dentro do prazo e com justificativa
  So that o organizador possa analisar a solicitacao com rastreabilidade

  Scenario: Abrir contestacao para partida com resultado oficial registrado
    Given que existe uma partida finalizada com resultado oficial registrado
    And que o usuario autenticado e responsavel por um dos times da partida
    When ele abrir uma contestacao de resultado com motivo justificativa e evidencias
    Then o sistema deve registrar a contestacao como pendente
    And a contestacao deve ficar associada a partida ao torneio ao time e ao usuario solicitante

  Scenario: Impedir contestacao de partida sem resultado oficial registrado
    Given que existe uma partida cadastrada sem resultado oficial registrado
    And que o usuario autenticado e responsavel por um dos times da partida
    When ele tentar abrir uma contestacao de resultado
    Then o sistema deve impedir a abertura da contestacao

  Scenario: Impedir contestacao por usuario que nao e responsavel por time da partida
    Given que existe uma partida finalizada com resultado oficial registrado
    And que o usuario autenticado nao e responsavel pelos times da partida
    When ele tentar abrir uma contestacao de resultado
    Then o sistema deve impedir a abertura da contestacao

  Scenario: Impedir contestacao fora do prazo definido pelo torneio
    Given que existe uma partida finalizada com resultado oficial registrado ha mais tempo que o prazo do torneio
    And que o usuario autenticado e responsavel por um dos times da partida
    When ele tentar abrir uma contestacao de resultado
    Then o sistema deve impedir a abertura da contestacao

  Scenario: Impedir contestacao duplicada pendente para o mesmo time e partida
    Given que existe uma partida finalizada com resultado oficial registrado
    And que o usuario autenticado e responsavel por um dos times da partida
    And que ja existe uma contestacao pendente daquele time para a partida
    When ele tentar abrir outra contestacao para a mesma partida
    Then o sistema deve impedir a abertura da contestacao

  Scenario: Organizador aceita contestacao e corrige resultado oficial
    Given que existe uma contestacao pendente de resultado
    And que o usuario autenticado e o organizador do torneio
    When o organizador aceitar a contestacao informando placar corrigido
    Then o sistema deve marcar a contestacao como aceita
    And deve registrar a decisao no historico da contestacao
    And deve atualizar o resultado oficial da partida

  Scenario: Organizador rejeita contestacao mantendo resultado oficial
    Given que existe uma contestacao pendente de resultado
    And que o usuario autenticado e o organizador do torneio
    When o organizador rejeitar a contestacao
    Then o sistema deve marcar a contestacao como rejeitada
    And deve registrar a decisao no historico da contestacao

  Scenario: Organizador solicita correcao ou mais informacoes
    Given que existe uma contestacao pendente de resultado
    And que o usuario autenticado e o organizador do torneio
    When o organizador solicitar correcao da contestacao
    Then o sistema deve marcar a contestacao como aguardando correcao
    And deve registrar a decisao no historico da contestacao

  Scenario: Impedir analise por usuario que nao e organizador do torneio
    Given que existe uma contestacao pendente de resultado
    And que o usuario autenticado nao e o organizador do torneio
    When ele tentar analisar a contestacao
    Then o sistema deve impedir a analise da contestacao
