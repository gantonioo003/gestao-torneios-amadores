# language: pt
Funcionalidade: Cadastrar profissional esportivo com registro de carreira

  Como usuario autenticado
  Quero cadastrar profissionais esportivos com historico de carreira
  Para que possam ser vinculados a times por seus responsaveis

  Cenario: Cadastrar profissional com nome e tipo validos
    Dado que o usuario esta autenticado
    Quando ele cadastrar um profissional esportivo com nome e tipo validos
    Entao o sistema deve registrar o profissional esportivo

  Cenario: Tentar cadastrar profissional sem nome
    Dado que o usuario esta autenticado
    Quando ele tentar cadastrar um profissional sem informar o nome
    Entao o sistema deve rejeitar o cadastro do profissional por nome invalido

  Cenario: Tentar cadastrar profissional sem tipo
    Dado que o usuario esta autenticado
    Quando ele tentar cadastrar um profissional sem informar o tipo
    Entao o sistema deve rejeitar o cadastro do profissional por tipo invalido

  Cenario: Apenas usuario autenticado pode cadastrar profissional
    Dado que o usuario nao esta autenticado
    Quando ele tentar cadastrar um profissional esportivo
    Entao o sistema deve exigir autenticacao

  Cenario: Cadastrante edita nome do profissional
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    Quando ele editar o nome do profissional
    Entao o sistema deve atualizar os dados do profissional

  Cenario: Outro usuario tenta editar o profissional
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado por outro usuario
    Quando ele tentar editar o profissional
    Entao o sistema deve impedir a operacao

  Cenario: Cadastrante remove o profissional sem vinculo ativo
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    Quando ele remover o profissional esportivo
    Entao o sistema deve excluir o profissional

  Cenario: Outro usuario tenta remover o profissional
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado por outro usuario
    Quando ele tentar remover o profissional
    Entao o sistema deve impedir a operacao

  Cenario: Adicionar registro de carreira com dados validos
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    Quando ele adicionar um registro de carreira com nome do clube data de inicio e motivo de saida validos
    Entao o sistema deve registrar o historico de carreira do profissional

  Cenario: Rejeitar registro de carreira sem nome do clube
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    Quando ele tentar adicionar um registro de carreira sem informar o nome do clube
    Entao o sistema deve rejeitar o registro por dados invalidos

  Cenario: Rejeitar registro de carreira sem data de inicio
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    Quando ele tentar adicionar um registro de carreira sem data de inicio
    Entao o sistema deve rejeitar o registro por dados invalidos

  Cenario: Rejeitar data de fim anterior a data de inicio
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    Quando ele tentar adicionar um registro com data de fim anterior a data de inicio
    Entao o sistema deve rejeitar o registro por periodo invalido

  Cenario: Rejeitar periodos sobrepostos no historico de carreira
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    E que o profissional ja possui um registro de carreira no periodo
    Quando ele tentar adicionar um registro de carreira com periodo sobreposto
    Entao o sistema deve rejeitar o registro por sobreposicao de periodos

  Cenario: Remover registro de carreira existente
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    E que o profissional ja possui um registro de carreira no periodo
    Quando ele remover o registro de carreira
    Entao o sistema deve excluir o registro de carreira do historico

  Cenario: Tentar remover registro de carreira inexistente
    Dado que o usuario esta autenticado
    E que existe um profissional esportivo cadastrado pelo usuario
    Quando ele tentar remover um registro de carreira que nao existe
    Entao o sistema deve informar que o registro nao foi encontrado