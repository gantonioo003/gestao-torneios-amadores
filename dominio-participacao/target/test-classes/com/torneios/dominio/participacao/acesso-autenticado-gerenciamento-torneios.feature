Feature: Permitir acesso autenticado Ã s funcionalidades de criaÃ§Ã£o e gerenciamento de torneios

  As a usuÃ¡rio da plataforma
  I want acessar autenticado as funcionalidades de criaÃ§Ã£o e gerenciamento
  So that eu possa criar e administrar meus torneios

  Scenario: UsuÃ¡rio autenticado acessa funcionalidade de criaÃ§Ã£o de torneio
    Given que o usuÃ¡rio estÃ¡ autenticado
    When ele solicitar acesso Ã  funcionalidade de criaÃ§Ã£o de torneio
    Then o sistema deve permitir o acesso

  Scenario: UsuÃ¡rio autenticado acessa seus torneios criados
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele possui torneios cadastrados
    When ele acessar a Ã¡rea de gerenciamento de torneios
    Then o sistema deve exibir os torneios criados por ele

  Scenario: UsuÃ¡rio nÃ£o autenticado tenta acessar funcionalidade de criaÃ§Ã£o de torneio
    Given que o usuÃ¡rio nÃ£o estÃ¡ autenticado
    When ele solicitar acesso Ã  funcionalidade de criaÃ§Ã£o de torneio
    Then o sistema deve exigir autenticaÃ§Ã£o

  Scenario: UsuÃ¡rio nÃ£o autenticado tenta acessar gerenciamento de torneios
    Given que o usuÃ¡rio nÃ£o estÃ¡ autenticado
    When ele tentar acessar a Ã¡rea de gerenciamento de torneios
    Then o sistema deve exigir autenticaÃ§Ã£o
