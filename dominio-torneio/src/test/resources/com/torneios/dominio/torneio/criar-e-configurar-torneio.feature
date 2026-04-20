Feature: Criar e configurar torneio

  As a usuÃ¡rio autenticado
  I want criar e configurar um torneio
  So that eu possa organizar uma competiÃ§Ã£o de futebol

  Scenario: Criar torneio completo com configuraÃ§Ã£o vÃ¡lida
    Given que o usuÃ¡rio estÃ¡ autenticado
    When ele criar um torneio informando nome, formato vÃ¡lido e formato de equipe 5x5
    And definir que o torneio aceita solicitaÃ§Ãµes de participaÃ§Ã£o
    Then o sistema deve registrar o torneio com sucesso
    And deve permitir entrada de times por solicitaÃ§Ã£o

  Scenario: Criar torneio com participantes previamente definidos
    Given que o usuÃ¡rio estÃ¡ autenticado
    When ele criar um torneio informando nome, formato vÃ¡lido e formato de equipe 11x11
    And informar os participantes iniciais
    Then o sistema deve registrar o torneio com os times definidos

  Scenario: Impedir criaÃ§Ã£o de torneio sem formato de competiÃ§Ã£o
    Given que o usuÃ¡rio estÃ¡ autenticado
    When ele tentar criar um torneio sem definir o formato da competiÃ§Ã£o
    Then o sistema deve impedir a criaÃ§Ã£o do torneio

  Scenario: Impedir criaÃ§Ã£o de torneio sem formato de equipe
    Given que o usuÃ¡rio estÃ¡ autenticado
    When ele tentar criar um torneio sem definir a quantidade de jogadores por equipe
    Then o sistema deve impedir a criaÃ§Ã£o do torneio
