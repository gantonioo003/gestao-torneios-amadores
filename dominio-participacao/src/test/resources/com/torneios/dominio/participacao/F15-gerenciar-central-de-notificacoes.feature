Feature: Gerenciar central de notificacoes e preferencias do usuario

  As a usuario autenticado
  I want acompanhar e organizar avisos persistidos da plataforma
  So that eu nao perca acontecimentos importantes e controle o que desejo receber

  Scenario: Receber notificacao de uma categoria habilitada
    Given que o usuario possui todas as categorias de notificacao habilitadas
    When o sistema enviar uma notificacao de torneio
    Then a notificacao deve ser salva como nao lida

  Scenario: Marcar uma notificacao como lida
    Given que o usuario possui uma notificacao nao lida
    When ele marcar a notificacao como lida
    Then a notificacao deve permanecer no historico como lida

  Scenario: Marcar todas as notificacoes como lidas
    Given que o usuario possui duas notificacoes nao lidas
    When ele marcar todas as notificacoes como lidas
    Then nenhuma notificacao ativa deve permanecer nao lida

  Scenario: Arquivar uma notificacao sem apagar o historico
    Given que o usuario possui uma notificacao nao lida
    When ele arquivar a notificacao
    Then ela deve sair da lista ativa e permanecer no historico arquivado

  Scenario: Nao gerar notificacao de categoria desativada
    Given que o usuario desativou notificacoes de amistoso
    When o sistema tentar enviar uma notificacao de amistoso
    Then nenhuma notificacao de amistoso deve ser salva

  Scenario: Impedir outro usuario de alterar a notificacao
    Given que o usuario possui uma notificacao nao lida
    When outro usuario tentar marcar a notificacao como lida
    Then o sistema deve impedir a operacao
