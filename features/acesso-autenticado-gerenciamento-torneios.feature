Feature: Permitir acesso autenticado às funcionalidades de criação e gerenciamento de torneios

  As a usuário da plataforma
  I want acessar autenticado as funcionalidades de criação e gerenciamento
  So that eu possa criar e administrar meus torneios

  Scenario: Usuário autenticado acessa funcionalidade de criação de torneio
    Given que o usuário está autenticado
    When ele solicitar acesso à funcionalidade de criação de torneio
    Then o sistema deve permitir o acesso

  Scenario: Usuário autenticado acessa seus torneios criados
    Given que o usuário está autenticado
    And que ele possui torneios cadastrados
    When ele acessar a área de gerenciamento de torneios
    Then o sistema deve exibir os torneios criados por ele

  Scenario: Usuário não autenticado tenta acessar funcionalidade de criação de torneio
    Given que o usuário não está autenticado
    When ele solicitar acesso à funcionalidade de criação de torneio
    Then o sistema deve exigir autenticação

  Scenario: Usuário não autenticado tenta acessar gerenciamento de torneios
    Given que o usuário não está autenticado
    When ele tentar acessar a área de gerenciamento de torneios
    Then o sistema deve exigir autenticação