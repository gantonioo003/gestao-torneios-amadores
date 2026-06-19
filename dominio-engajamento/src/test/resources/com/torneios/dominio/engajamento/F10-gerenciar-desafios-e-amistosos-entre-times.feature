Feature: Gerenciar confrontos amistosos entre times por contas de treinador

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

  Scenario: Acompanhar confrontos enviados e recebidos no proprio time
    Given que existe um convite de amistoso pendente
    When o responsavel acompanhar os confrontos do seu time
    Then o sistema deve listar o convite de amistoso

  Scenario: Cancelar desafio enviado
    Given que existe um convite de amistoso pendente
    When o responsavel do time desafiante cancelar o desafio
    Then o sistema deve marcar o desafio como cancelado

  Scenario: Impedir dois desafios abertos entre os mesmos times
    Given que existe um convite de amistoso pendente
    When o responsavel tentar enviar outro desafio entre os mesmos times
    Then o sistema deve impedir a operacao

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

  Scenario: Impedir proposta por conta comum
    Given que existe um time desafiante ligado a uma conta comum
    When ele propor um confronto amistoso para outro time
    Then o sistema deve impedir a operacao

  Scenario: Impedir aceite por usuario sem responsabilidade pelos times
    Given que existe um convite de amistoso pendente
    When um usuario sem responsabilidade pelos times tentar aceitar o convite
    Then o sistema deve impedir a operacao
