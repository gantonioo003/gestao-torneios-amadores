Feature: Gerenciar comunicados e feed social do torneio

  As a usuario da plataforma
  I want acompanhar comunicados, comentarios e atualizacoes automaticas dos jogos
  So that o torneio tenha um feed social vivo e centralizado

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
