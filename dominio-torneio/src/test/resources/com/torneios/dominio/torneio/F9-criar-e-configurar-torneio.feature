Feature: Gerenciar criacao, configuracao e repeticao do torneio

  As a usuario autenticado organizador
  I want criar, configurar e repetir um torneio
  So that eu possa organizar novas edicoes sem perder o historico anterior

  Scenario: Criar torneio completo com configuracao valida
    Given que o usuario esta autenticado
    When ele criar um torneio informando nome, formato valido e formato de equipe 5x5
    And definir que o torneio aceita solicitacoes de participacao
    Then o sistema deve registrar o torneio com sucesso
    And deve permitir entrada de times por solicitacao

  Scenario: Criar torneio com participantes previamente definidos
    Given que o usuario esta autenticado
    When ele criar um torneio informando nome, formato valido e formato de equipe 11x11
    And informar os participantes iniciais
    Then o sistema deve registrar o torneio com os times definidos

  Scenario: Gerar estrutura do torneio por sorteio
    Given que existe um torneio com formato fase de grupos com mata-mata
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competicao por sorteio
    Then o sistema deve gerar a estrutura sorteando os times

  Scenario: Gerar estrutura do torneio por montagem manual
    Given que existe um torneio com formato fase de grupos com mata-mata
    And que o torneio possui participantes suficientes
    When o organizador gerar a estrutura da competicao escolhendo manualmente a ordem dos times
    Then o sistema deve gerar a estrutura respeitando a montagem manual

  Scenario: Repetir torneio mantendo historico da edicao anterior
    Given que existe um torneio finalizado
    When o organizador repetir o torneio para uma nova edicao
    Then o sistema deve arquivar a edicao anterior e reiniciar o torneio sem participantes

  Scenario: Impedir criacao de torneio sem formato de competicao
    Given que o usuario esta autenticado
    When ele tentar criar um torneio sem definir o formato da competicao
    Then o sistema deve impedir a criacao do torneio

  Scenario: Impedir criacao de torneio sem formato de equipe
    Given que o usuario esta autenticado
    When ele tentar criar um torneio sem definir a quantidade de jogadores por equipe
    Then o sistema deve impedir a criacao do torneio
