Feature: Visualizar torneios disponÃ­veis na plataforma

  As a visitante ou usuÃ¡rio autenticado
  I want visualizar os torneios disponÃ­veis na plataforma
  So that eu possa conhecer as competiÃ§Ãµes existentes

  Scenario: Visitante visualiza torneios disponÃ­veis
    Given que existem torneios cadastrados na plataforma
    When o visitante acessar a pÃ¡gina inicial
    Then o sistema deve exibir os torneios disponÃ­veis para visualizaÃ§Ã£o

  Scenario: UsuÃ¡rio autenticado visualiza torneios disponÃ­veis
    Given que existem torneios cadastrados na plataforma
    And que o usuÃ¡rio estÃ¡ autenticado
    When o usuÃ¡rio acessar a pÃ¡gina inicial
    Then o sistema deve exibir os torneios disponÃ­veis para visualizaÃ§Ã£o

  Scenario: Nenhum torneio disponÃ­vel para visualizaÃ§Ã£o
    Given que nÃ£o existem torneios cadastrados na plataforma
    When o visitante acessar a pÃ¡gina inicial
    Then o sistema deve informar que nÃ£o hÃ¡ torneios disponÃ­veis no momento
