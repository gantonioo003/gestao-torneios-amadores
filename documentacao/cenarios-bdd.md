# Cenários BDD

## Funcionalidades com cenários definidos

### F1. Calcular automaticamente a nota estatística dos jogadores
Arquivo: features/calcular-nota-estatistica.feature

Cenário principal:
- calcular a nota estatística do jogador com base em eventos registrados

### F2. Registrar gols e assistências da partida
Arquivo: features/registrar-eventos-partida.feature

Cenário principal:
- registrar gol e assistência de jogadores em uma partida válida

## Regras de negócio cobertas

- RN07. O sistema deve permitir registrar gols, assistências, cartões amarelos e cartões vermelhos dos jogadores em cada partida.
- RN08. A nota estatística de cada jogador deve ser calculada automaticamente a partir dos eventos registrados na partida.
- RN09. A nota estatística inicial do jogador em uma partida é 5,0.
- RN10. Gols e assistências aumentam a nota estatística do jogador.
- RN11. Cartões amarelos e vermelhos reduzem a nota estatística do jogador.