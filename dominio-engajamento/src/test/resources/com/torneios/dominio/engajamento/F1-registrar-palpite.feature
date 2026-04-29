Feature: Gerenciar palpites publicos sobre partidas e torneios

  As a visitante ou usuario autenticado
  I want registrar palpites sobre vencedores de partidas, campeao do torneio, artilheiro e lider de assistencias
  So that eu possa participar ativamente da competicao e acompanhar minha taxa de acerto

  Scenario: Registrar palpite de usuario autenticado sobre vencedor de partida
    Given que o usuario esta autenticado
    And que existe uma partida cadastrada com janela de votacao aberta
    When ele registrar um palpite indicando o time vencedor da partida
    Then o sistema deve armazenar o palpite do usuario para a partida

  Scenario: Registrar palpite sobre campeao do torneio com sucesso
    Given que o usuario esta autenticado
    And que existe um torneio que ainda nao foi iniciado
    When ele registrar um palpite indicando o time campeao do torneio
    Then o sistema deve armazenar o palpite do usuario para o torneio

  Scenario: Registrar palpite sobre artilheiro do torneio com sucesso
    Given que o usuario esta autenticado
    And que existe um torneio que ainda nao foi iniciado
    When ele registrar um palpite indicando o jogador artilheiro do torneio
    Then o sistema deve armazenar o palpite do usuario para o torneio

  Scenario: Registrar palpite sobre lider de assistencias do torneio com sucesso
    Given que o usuario esta autenticado
    And que existe um torneio que ainda nao foi iniciado
    When ele registrar um palpite indicando o jogador lider de assistencias do torneio
    Then o sistema deve armazenar o palpite do usuario para o torneio

  Scenario: Registrar palpite de visitante nao autenticado sobre vencedor de partida
    Given que o visitante nao esta autenticado
    And que existe uma partida cadastrada com janela de votacao aberta
    When ele registrar um palpite publico indicando o time vencedor da partida
    Then o sistema deve armazenar o palpite do visitante para a partida

  Scenario: Substituir palpite anterior do mesmo usuario para o mesmo evento alvo
    Given que o usuario esta autenticado
    And que ele ja registrou um palpite para um evento com janela de votacao aberta
    When ele registrar um novo palpite para o mesmo evento alvo
    Then o sistema deve manter apenas o palpite mais recente do usuario para aquele evento

  Scenario: Alterar palpite enquanto a janela de votacao estiver aberta
    Given que o usuario esta autenticado
    And que ele ja registrou um palpite para um evento com janela de votacao aberta
    When ele alterar a opcao do palpite
    Then o sistema deve atualizar o palpite do usuario para o evento

  Scenario: Impedir alteracao de palpite apos o fechamento da janela de votacao
    Given que o usuario esta autenticado
    And que existe uma partida que ja foi iniciada
    When ele tentar registrar ou alterar um palpite sobre o vencedor dessa partida
    Then o sistema deve impedir a operacao

  Scenario: Impedir palpite com opcao invalida para o evento alvo
    Given que o usuario esta autenticado
    And que existe uma partida cadastrada com janela de votacao aberta
    When ele tentar registrar um palpite indicando um time que nao participa da partida
    Then o sistema deve impedir a operacao

  Scenario: Exibir percentual atualizado por opcao enquanto a janela estiver aberta
    Given que existem multiplos palpites registrados para um mesmo evento alvo
    When o sistema for consultado pelos percentuais do evento
    Then ele deve retornar a distribuicao percentual de votos por opcao

  Scenario: Apurar palpite como acertado quando a opcao escolhida coincide com o resultado real
    Given que existe um palpite registrado para um evento ja concluido
    And que a opcao escolhida pelo usuario corresponde ao resultado real
    When o sistema apurar o evento alvo
    Then o palpite do usuario deve ser marcado como acertado

  Scenario: Apurar palpite como nao acertado quando a opcao escolhida diverge do resultado real
    Given que existe um palpite registrado para um evento ja concluido
    And que a opcao escolhida pelo usuario nao corresponde ao resultado real
    When o sistema apurar o evento alvo
    Then o palpite do usuario deve ser marcado como nao acertado

  Scenario: Impedir alteracao de um palpite apos a apuracao
    Given que existe um palpite ja apurado
    When o usuario tentar alterar a opcao desse palpite
    Then o sistema deve impedir a operacao
