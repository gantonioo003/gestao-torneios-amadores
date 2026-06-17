Feature: Visualizar torneios disponíveis na plataforma

  As a visitante ou usuário autenticado
  I want visualizar os torneios disponíveis na plataforma
  So that eu possa conhecer as competições existentes

  Scenario: Visitante visualiza torneios disponíveis
    Given que existem torneios cadastrados na plataforma
    When o visitante acessar a página inicial
    Then o sistema deve exibir os torneios disponíveis para visualização

  Scenario: Usuário autenticado visualiza torneios disponíveis
    Given que existem torneios cadastrados na plataforma
    And que o usuário está autenticado
    When o usuário acessar a página inicial
    Then o sistema deve exibir os torneios disponíveis para visualização

  Scenario: Nenhum torneio disponível para visualização
    Given que não existem torneios cadastrados na plataforma
    When o visitante acessar a página inicial
    Then o sistema deve informar que não há torneios disponíveis no momento