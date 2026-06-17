# Feature: Criar um torneio
# Autor: Guilherme Rabelo

Feature: Criar um torneio

Scenario: Criar um novo torneio com formato de mata-mata
    Given que o organizador deseja iniciar uma nova competição
    When ele criar um torneio com o formato "mata-mata"
    Then o sistema deve registrar o torneio com sucesso
    And o formato associado ao torneio deve ser "mata-mata"

Scenario: Criar um novo torneio com formato de fase de grupos e mata-mata
    Given que o organizador deseja iniciar uma nova competição
    When ele criar um torneio com o formato "fase de grupos com mata-mata"
    Then o sistema deve registrar o torneio com sucesso
    And o formato associado ao torneio deve ser "fase de grupos com mata-mata"

Scenario: Criar um novo torneio com formato de pontos corridos
    Given que o organizador deseja iniciar uma nova competição
    When ele criar um torneio com o formato "pontos corridos"
    Then o sistema deve registrar o torneio com sucesso
    And o formato associado ao torneio deve ser "pontos corridos"

Scenario: Criar um novo torneio com formato de final única
    Given que o organizador deseja iniciar uma nova competição
    When ele criar um torneio com o formato "final única"
    Then o sistema deve registrar o torneio com sucesso
    And o formato associado ao torneio deve ser "final única"

Scenario: Tentar criar um torneio sem formato definido
    Given que o organizador deseja iniciar uma nova competição
    When ele tentar criar um torneio sem informar o formato
    Then o sistema deve exibir uma mensagem de erro "Todo torneio deve possuir um formato definido"
    And o torneio não deve ser salvo
