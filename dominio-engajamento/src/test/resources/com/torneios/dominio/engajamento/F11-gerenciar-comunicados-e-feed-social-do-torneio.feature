Feature: Gerenciar ecossistema de feed social da plataforma e dos torneios

  As a usuario da plataforma
  I want acompanhar e interagir com publicacoes sobre torneios, partidas e peladas
  So that a plataforma funcione como uma rede social de futebol amador

  Scenario: Publicar postagem no feed social geral
    Given que o usuario esta autenticado
    When ele publicar uma postagem no feed social com hashtag e midia
    Then o sistema deve armazenar a postagem no feed geral

  Scenario: Publicar comunicado oficial no feed do torneio
    Given que existe um torneio com organizador autenticado
    When o organizador publicar um comunicado oficial no feed do torneio
    Then o sistema deve armazenar o comunicado no feed do torneio

  Scenario: Impedir comunicado oficial por usuario que nao e organizador
    Given que existe um torneio com organizador autenticado
    And que o usuario autenticado nao e o organizador do torneio
    When ele tentar publicar um comunicado oficial no feed do torneio
    Then o sistema deve impedir a operacao

  Scenario: Comentar sobre uma partida do torneio
    Given que existe uma partida cadastrada no torneio
    And que o usuario esta autenticado
    When ele comentar sobre a partida no feed social
    Then o sistema deve armazenar o comentario vinculado a partida

  Scenario: Impedir comentario de usuario nao autenticado
    Given que existe uma partida cadastrada no torneio
    And que o usuario nao esta autenticado
    When ele tentar comentar sobre a partida no feed social
    Then o sistema deve impedir a operacao

  Scenario: Publicar atualizacao automatica sobre jogo
    Given que existe uma partida cadastrada no torneio
    When o sistema registrar uma atualizacao automatica sobre o resultado do jogo
    Then o feed deve exibir a atualizacao automatica da partida

  Scenario: Editar comentario pelo proprio autor
    Given que existe um comentario publicado pelo usuario no feed social
    When o usuario editar o proprio comentario
    Then o sistema deve atualizar o comentario no feed do torneio

  Scenario: Listar publicacoes do feed do torneio
    Given que existem comunicados, comentarios e atualizacoes automaticas no torneio
    When o usuario acessar o feed social do torneio
    Then o sistema deve listar as publicacoes do torneio

  Scenario: Visitante visualizar feed geral sem interagir
    Given que existe uma postagem publicada no feed social geral
    When o visitante acessar o feed social geral
    Then o sistema deve listar as publicacoes publicas

  Scenario: Usuario autenticado curtir e reagir em publicacao do feed
    Given que existe uma postagem publicada no feed social geral
    And que o usuario esta autenticado
    When ele curtir e reagir a publicacao
    Then o sistema deve registrar a curtida e a reacao

  Scenario: Impedir interacao de visitante no feed
    Given que existe uma postagem publicada no feed social geral
    And que o visitante nao esta autenticado
    When ele tentar curtir a publicacao
    Then o sistema deve impedir a operacao

  Scenario: Filtrar publicacoes por hashtag
    Given que existem postagens com hashtags diferentes no feed social geral
    When o usuario filtrar o feed por uma hashtag
    Then o sistema deve listar apenas publicacoes daquela hashtag

  Scenario: Publicar como time administrado pelo treinador
    Given que o usuario autenticado e responsavel por um time
    When ele publicar uma postagem representando o time
    Then o sistema deve salvar a publicacao com identidade do time

  Scenario: Impedir publicacao usando time de outro responsavel
    Given que existe um time administrado por outro usuario
    When o usuario tentar publicar representando esse time
    Then o sistema deve impedir a operacao

  Scenario: Comentar uma publicacao apenas com foto
    Given que existe uma postagem publicada no feed social geral
    When o usuario responder a publicacao apenas com uma foto
    Then o comentario com foto deve ser salvo na publicacao

  Scenario: Alternar uma unica curtida por conta
    Given que existe uma postagem publicada no feed social geral
    When o usuario curtir a publicacao duas vezes
    Then o sistema deve remover a curtida no segundo clique sem duplicar

  Scenario: Listar apenas postagens pessoais no perfil
    Given que o usuario publicou como pessoa e representando um time
    When o perfil consultar as publicacoes pessoais do usuario
    Then o sistema deve listar apenas a postagem feita como usuario

  Scenario: Disponibilizar publicacao ativa para encaminhamento no chat
    Given que existe uma postagem publicada no feed social geral
    When o chat consultar a publicacao encaminhada
    Then o sistema deve retornar a publicacao ativa para o card da mensagem
