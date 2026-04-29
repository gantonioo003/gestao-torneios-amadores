Feature: Gerenciar conta de usuario e autenticacao

  As a usuario da plataforma
  I want cadastrar, acessar, editar e excluir minha conta
  So that eu possa controlar meu acesso e meus dados no sistema

  Scenario: Cadastrar nova conta de usuario
    Given que nao existe conta cadastrada para o email informado
    When o usuario cadastrar uma nova conta com nome email e senha validos
    Then o sistema deve criar a conta do usuario

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
