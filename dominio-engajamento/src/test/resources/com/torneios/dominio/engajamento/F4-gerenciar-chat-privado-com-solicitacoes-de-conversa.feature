Feature: Gerenciar chat privado com solicitacoes de conversa

  As a usuario autenticado da plataforma
  I want solicitar conversas privadas e controlar quem pode me enviar mensagens
  So that o chat funcione como uma caixa de solicitados antes de liberar a conversa

  Scenario: Solicitar conversa privada com outro usuario
    Given que o usuario esta autenticado
    And que existe outro usuario cadastrado na plataforma
    When ele solicitar uma conversa privada com esse usuario
    Then o sistema deve registrar a conversa como solicitada
    And deve exibir a conversa na aba de solicitados do destinatario

  Scenario: Aprovar solicitacao de conversa
    Given que existe uma solicitacao de conversa pendente para o usuario
    When o destinatario aprovar a solicitacao de conversa
    Then o sistema deve liberar a troca de mensagens

  Scenario: Recusar solicitacao de conversa
    Given que existe uma solicitacao de conversa pendente para o usuario
    When o destinatario recusar a solicitacao de conversa
    Then o sistema deve manter a conversa bloqueada para mensagens

  Scenario: Enviar mensagem em conversa aprovada
    Given que existe uma conversa aprovada entre dois usuarios
    When ele enviar uma mensagem na conversa aprovada
    Then o sistema deve armazenar a mensagem na conversa

  Scenario: Impedir mensagem antes da aprovacao da conversa
    Given que existe uma solicitacao de conversa pendente para o usuario
    When ele tentar enviar uma mensagem antes da aprovacao
    Then o sistema deve impedir a operacao

  Scenario: Consultar historico de conversas aprovadas
    Given que existe uma conversa aprovada entre dois usuarios
    When o usuario consultar suas conversas aprovadas
    Then o sistema deve listar a conversa no historico do usuario

  Scenario: Impedir solicitacao de conversa por usuario nao autenticado
    Given que o usuario nao esta autenticado
    And que existe outro usuario cadastrado na plataforma
    When ele tentar solicitar uma conversa privada com esse usuario
    Then o sistema deve impedir a operacao
