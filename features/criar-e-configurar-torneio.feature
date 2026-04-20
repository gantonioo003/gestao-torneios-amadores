Feature: Criar e configurar torneio

  As a usuário autenticado
  I want criar e configurar um torneio
  So that eu possa organizar uma competição de futebol

  Scenario: Criar torneio com formato válido
    Given que o usuário está autenticado
    When ele criar um torneio informando nome e formato válido
    Then o sistema deve registrar o torneio com sucesso

  Scenario: Impedir criação de torneio sem formato definido
    Given que o usuário está autenticado
    When ele tentar criar um torneio sem definir formato
    Then o sistema deve impedir a criação do torneio

  Scenario: Criar torneio com vagas abertas para solicitação de participação
    Given que o usuário está autenticado
    When ele criar um torneio configurado para aceitar solicitações de participação
    Then o sistema deve registrar o torneio com vagas abertas

  Scenario: Criar torneio com participantes previamente definidos
    Given que o usuário está autenticado
    When ele criar um torneio informando os participantes iniciais
    Then o sistema deve registrar o torneio com os times definidos