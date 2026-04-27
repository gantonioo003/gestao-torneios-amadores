Feature: Criar e configurar torneio

  As a usuário autenticado
  I want criar e configurar um torneio
  So that eu possa organizar uma competição de futebol

  Scenario: Criar torneio completo com configuração válida
    Given que o usuário está autenticado
    When ele criar um torneio informando nome, formato válido e formato de equipe 5x5
    And definir que o torneio aceita solicitações de participação
    Then o sistema deve registrar o torneio com sucesso
    And deve permitir entrada de times por solicitação

  Scenario: Criar torneio com participantes previamente definidos
    Given que o usuário está autenticado
    When ele criar um torneio informando nome, formato válido e formato de equipe 11x11
    And informar os participantes iniciais
    Then o sistema deve registrar o torneio com os times definidos

  Scenario: Impedir criação de torneio sem formato de competição
    Given que o usuário está autenticado
    When ele tentar criar um torneio sem definir o formato da competição
    Then o sistema deve impedir a criação do torneio

  Scenario: Impedir criação de torneio sem formato de equipe
    Given que o usuário está autenticado
    When ele tentar criar um torneio sem definir a quantidade de jogadores por equipe
    Then o sistema deve impedir a criação do torneio