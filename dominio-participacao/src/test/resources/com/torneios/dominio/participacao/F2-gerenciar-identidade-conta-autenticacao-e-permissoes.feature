Feature: Gerenciar identidade, conta, autenticacao, perfil e permissoes

  As a usuario da plataforma
  I want cadastrar, acessar e manter minha identidade conforme minha funcao no futebol
  So that eu possa controlar meus dados e acessar apenas as funcoes permitidas ao meu perfil

  Scenario: Cadastrar nova conta de usuario
    Given que nao existe conta cadastrada para o email informado
    When o usuario cadastrar uma nova conta com nome email e senha validos
    Then o sistema deve criar a conta do usuario

  Scenario: Cadastrar conta do tipo jogador
    Given que nao existe conta cadastrada para o email informado
    When o usuario cadastrar uma nova conta do tipo jogador
    Then o sistema deve criar a conta como jogador

  Scenario: Cadastrar conta do tipo organizador
    Given que nao existe conta cadastrada para o email informado
    When o usuario cadastrar uma nova conta do tipo organizador
    Then o sistema deve criar a conta como organizador

  Scenario: Cadastrar conta do tipo treinador
    Given que nao existe conta cadastrada para o email informado
    When o usuario cadastrar uma nova conta do tipo treinador
    Then o sistema deve criar a conta como treinador

  Scenario: Cadastrar conta comum apenas para acompanhar e interagir
    Given que nao existe conta cadastrada para o email informado
    When o usuario cadastrar uma conta comum sem funcao esportiva
    Then o sistema deve criar a conta sem funcao esportiva

  Scenario: Realizar login com email e senha validos
    Given que existe uma conta cadastrada para o usuario
    When ele informar email e senha validos
    Then o sistema deve autenticar o usuario

  Scenario: Impedir login com senha incorreta
    Given que existe uma conta cadastrada para o usuario
    When ele informar senha incorreta
    Then o sistema deve impedir a autenticacao

  Scenario: Editar dados da conta
    Given que existe uma conta cadastrada para o usuario
    When ele editar nome e email da conta
    Then o sistema deve atualizar os dados da conta

  Scenario: Excluir conta de usuario
    Given que existe uma conta cadastrada para o usuario
    When ele solicitar a exclusao da conta
    Then o sistema deve remover a conta e impedir novo login

  Scenario: Impedir cadastro com email ja utilizado
    Given que existe uma conta cadastrada para o usuario
    When outro usuario tentar cadastrar conta com o mesmo email
    Then o sistema deve impedir o cadastro da conta

  Scenario: Permitir criacao de torneio apenas ao organizador
    Given que existe uma conta organizadora cadastrada
    When o sistema verificar a permissao para criar torneios
    Then a conta deve possuir permissao para criar torneios

  Scenario: Impedir jogador de gerenciar times
    Given que existe uma conta de jogador cadastrada
    When o sistema verificar a permissao para gerenciar times
    Then o sistema deve impedir a operacao

  Scenario: Permitir gestao de times apenas ao treinador
    Given que existe uma conta de treinador cadastrada
    When o sistema verificar a permissao para gerenciar times
    Then a conta deve possuir permissao para gerenciar times

  Scenario: Impedir organizador de gerenciar times
    Given que existe uma conta organizadora cadastrada
    When o sistema verificar a permissao para gerenciar times
    Then o sistema deve impedir a operacao

  Scenario: Impedir conta comum de criar torneios
    Given que existe uma conta comum cadastrada
    When o sistema verificar a permissao para criar torneios
    Then o sistema deve impedir a operacao

  Scenario: Impedir conta comum de gerenciar times
    Given que existe uma conta comum cadastrada
    When o sistema verificar a permissao para gerenciar times
    Then o sistema deve impedir a operacao

  Scenario: Salvar torneio no perfil da conta
    Given que existe uma conta cadastrada para o usuario
    When o usuario salvar um torneio para acompanhar depois
    Then o torneio deve aparecer entre os torneios salvos da conta

  Scenario: Remover torneio salvo do perfil da conta
    Given que existe uma conta cadastrada para o usuario
    And que a conta possui um torneio salvo
    When o usuario remover o torneio dos salvos
    Then o torneio nao deve permanecer entre os torneios salvos da conta
