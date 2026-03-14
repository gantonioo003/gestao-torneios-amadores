# Cenários BDD

## Funcionalidades com cenários definidos

### F1. Calcular automaticamente a nota estatística dos jogadores
Arquivo: features/calcular-nota-estatistica.feature

Cenário principal:
- calcular a nota estatística do jogador com base nos eventos registrados na partida utilizando pesos definidos para cada tipo de evento.

### F2. Registrar gols e assistências da partida
Arquivo: features/registrar-gols-assistencias.feature

Cenário principal:
- registrar gol e assistência de jogadores em uma partida válida.

### F3. Registrar cartões amarelos e vermelhos da partida
Arquivo: features/registrar-cartoes.feature

Cenário principal:
- registrar cartão amarelo e cartão vermelho para jogadores em uma partida válida.

---

## Regras de negócio cobertas

- RN07. O sistema deve permitir registrar gols, assistências, cartões amarelos e cartões vermelhos dos jogadores em cada partida.
- RN08. A nota estatística de cada jogador deve ser calculada automaticamente a partir dos eventos registrados na partida.
- RN09. A nota estatística do jogador deve ser calculada por meio de uma fórmula baseada em pesos associados aos eventos registrados na partida.
- RN10. Na versão inicial do sistema, a fórmula de cálculo da nota estatística deve considerar gols, assistências, cartões amarelos e cartões vermelhos.
- RN11. Eventos positivos devem aumentar a nota estatística do jogador, enquanto eventos negativos devem reduzi-la.
