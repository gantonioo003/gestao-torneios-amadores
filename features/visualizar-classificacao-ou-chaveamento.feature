Feature: Atualizar e visualizar classificação ou chaveamento da competição

  As a usuário da plataforma
  I want visualizar a classificação ou chaveamento da competição
  So that eu possa acompanhar o andamento do torneio

  Scenario: Visualizar classificação em torneio de pontos corridos
    Given que existe um torneio com formato pontos corridos
    When o usuário acessar a classificação
    Then o sistema deve exibir a tabela com a pontuação dos times

  Scenario: Visualizar chaveamento em torneio mata-mata
    Given que existe um torneio com formato mata-mata
    When o usuário acessar o chaveamento
    Then o sistema deve exibir a estrutura eliminatória do torneio

  Scenario: Atualizar classificação após resultado de partida
    Given que um resultado de partida foi registrado
    When o usuário acessar a classificação
    Then o sistema deve exibir a classificação atualizada

  Scenario: Impedir visualização sem estrutura gerada
    Given que o torneio ainda não possui estrutura definida
    When o usuário acessar classificação ou chaveamento
    Then o sistema deve informar que a competição ainda não foi iniciada