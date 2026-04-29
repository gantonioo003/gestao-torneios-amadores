Feature: Gerenciar desafios e amistosos entre times

  As a responsavel por um time
  I want propor, aceitar, agendar e registrar resultados de amistosos
  So that os times criem historico e encontrem adversarios fora dos torneios

  Scenario: Propor confronto amistoso para outro time
    Given que existe um time desafiante com responsavel autenticado
    When ele propor um confronto amistoso para outro time
    Then o sistema deve registrar o desafio como proposto

  Scenario: Aceitar convite de amistoso
    Given que existe um convite de amistoso pendente
    When o responsavel do time desafiado aceitar o convite
    Then o sistema deve marcar o amistoso como aceito

  Scenario: Recusar convite de amistoso
    Given que existe um convite de amistoso pendente
    When o responsavel do time desafiado recusar o convite
    Then o sistema deve marcar o convite como recusado

  Scenario: Reagendar amistoso aceito
    Given que existe um amistoso aceito entre os times
    When um responsavel reagendar o amistoso
    Then o sistema deve atualizar data e local do amistoso

  Scenario: Registrar resultado no historico dos times
    Given que existe um amistoso aceito entre os times
    When um responsavel registrar o resultado do amistoso
    Then o sistema deve salvar o placar no historico dos times

  Scenario: Impedir desafio contra o proprio time
    Given que existe um time desafiante com responsavel autenticado
    When ele tentar desafiar o proprio time
    Then o sistema deve impedir a operacao

  Scenario: Impedir aceite por usuario sem responsabilidade pelos times
    Given que existe um convite de amistoso pendente
    When um usuario sem responsabilidade pelos times tentar aceitar o convite
    Then o sistema deve impedir a operacao
