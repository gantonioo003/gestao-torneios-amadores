Feature: Gerenciar times do usuÃ¡rio

  As a usuÃ¡rio autenticado
  I want gerenciar meus times
  So that eu possa utilizÃ¡-los em torneios

  Scenario: Criar um novo time com sucesso
    Given que o usuÃ¡rio estÃ¡ autenticado
    When ele cadastrar um novo time com informaÃ§Ãµes vÃ¡lidas
    Then o sistema deve registrar o time para esse usuÃ¡rio

  Scenario: Editar informaÃ§Ãµes de um time do usuÃ¡rio
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele possui um time cadastrado
    When ele alterar as informaÃ§Ãµes do time
    Then o sistema deve atualizar os dados do time

  Scenario: Excluir um time do usuÃ¡rio sem vÃ­nculo em torneio
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele possui um time cadastrado
    And que o time nÃ£o estÃ¡ vinculado a nenhum torneio
    When ele solicitar a exclusÃ£o do time
    Then o sistema deve remover o time

  Scenario: Impedir exclusÃ£o de time vinculado a torneio
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele possui um time cadastrado
    And que o time estÃ¡ vinculado a um torneio
    When ele solicitar a exclusÃ£o do time
    Then o sistema deve impedir a exclusÃ£o
