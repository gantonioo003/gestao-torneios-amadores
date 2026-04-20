Feature: Atualizar e visualizar classificaÃ§Ã£o ou chaveamento da competiÃ§Ã£o

  As a usuÃ¡rio da plataforma
  I want visualizar a classificaÃ§Ã£o ou chaveamento da competiÃ§Ã£o
  So that eu possa acompanhar o andamento do torneio

  Scenario: Visualizar classificaÃ§Ã£o em torneio de pontos corridos
    Given que existe um torneio com formato pontos corridos
    When o usuÃ¡rio acessar a classificaÃ§Ã£o
    Then o sistema deve exibir a tabela com a pontuaÃ§Ã£o dos times

  Scenario: Visualizar chaveamento em torneio mata-mata
    Given que existe um torneio com formato mata-mata
    When o usuÃ¡rio acessar o chaveamento
    Then o sistema deve exibir a estrutura eliminatÃ³ria do torneio

  Scenario: Atualizar classificaÃ§Ã£o apÃ³s resultado de partida
    Given que um resultado de partida foi registrado
    When o usuÃ¡rio acessar a classificaÃ§Ã£o
    Then o sistema deve exibir a classificaÃ§Ã£o atualizada

  Scenario: Impedir visualizaÃ§Ã£o sem estrutura gerada
    Given que o torneio ainda nÃ£o possui estrutura definida
    When o usuÃ¡rio acessar classificaÃ§Ã£o ou chaveamento
    Then o sistema deve informar que a competiÃ§Ã£o ainda nÃ£o foi iniciada
