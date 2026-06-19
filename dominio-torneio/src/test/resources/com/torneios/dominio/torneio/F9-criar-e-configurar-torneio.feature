Feature: Gerenciar torneios pela conta organizadora da criacao a nova edicao

  As a usuario autenticado organizador
  I want criar, configurar, repetir e preparar a competicao de um torneio
  So that eu possa organizar novas edicoes e preparar disputas sem perder o historico anterior

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

  Scenario: Organizador editar dados internos antes do inicio
    Given que existe um torneio configurado
    When o organizador alterar o nome e a regra de entrada do torneio
    Then o sistema deve atualizar os dados internos sem alterar os formatos

  Scenario: Impedir outro usuario de editar o torneio
    Given que existe um torneio configurado
    When outro usuario tentar editar os dados internos do torneio
    Then o sistema deve impedir a operacao

  Scenario: Impedir edicao estrutural depois do inicio
    Given que existe um torneio ja iniciado
    When o organizador tentar editar os dados internos do torneio
    Then o sistema deve impedir a operacao

  Scenario: Regerar competicao depois de ajustar participantes
    Given que existe um torneio com estrutura gerada
    When o organizador remover um participante antes do inicio
    Then o sistema deve invalidar a preparacao anterior

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
