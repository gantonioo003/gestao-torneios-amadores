Feature: Solicitar participaÃ§Ã£o de um time em torneio aberto

  As a usuÃ¡rio autenticado
  I want solicitar a participaÃ§Ã£o do meu time em um torneio aberto
  So that eu possa disputar a competiÃ§Ã£o

  Scenario: UsuÃ¡rio autenticado solicita participaÃ§Ã£o com time cadastrado
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele possui um time cadastrado
    And que o torneio estÃ¡ com vagas abertas para solicitaÃ§Ã£o de participaÃ§Ã£o
    When o usuÃ¡rio solicitar a participaÃ§Ã£o do seu time no torneio
    Then o sistema deve registrar a solicitaÃ§Ã£o de participaÃ§Ã£o

  Scenario: UsuÃ¡rio sem time cadastrado tenta solicitar participaÃ§Ã£o
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele nÃ£o possui time cadastrado
    And que o torneio estÃ¡ com vagas abertas para solicitaÃ§Ã£o de participaÃ§Ã£o
    When o usuÃ¡rio solicitar a participaÃ§Ã£o em um torneio
    Then o sistema deve impedir a solicitaÃ§Ã£o
    And deve informar que Ã© necessÃ¡rio possuir um time cadastrado

  Scenario: UsuÃ¡rio nÃ£o autenticado tenta solicitar participaÃ§Ã£o
    Given que o usuÃ¡rio nÃ£o estÃ¡ autenticado
    And que o torneio estÃ¡ com vagas abertas para solicitaÃ§Ã£o de participaÃ§Ã£o
    When ele solicitar participaÃ§Ã£o em um torneio
    Then o sistema deve exigir autenticaÃ§Ã£o

  Scenario: UsuÃ¡rio tenta solicitar participaÃ§Ã£o em torneio sem vagas abertas
    Given que o usuÃ¡rio estÃ¡ autenticado
    And que ele possui um time cadastrado
    And que o torneio nÃ£o estÃ¡ com vagas abertas para solicitaÃ§Ã£o de participaÃ§Ã£o
    When o usuÃ¡rio solicitar a participaÃ§Ã£o do seu time no torneio
    Then o sistema deve impedir a solicitaÃ§Ã£o
