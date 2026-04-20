# Regras de Negócio

## Acesso e participação

RN01. Usuários não autenticados podem visualizar os torneios disponíveis na página inicial do sistema.

RN02. Apenas usuários autenticados podem criar torneios.

RN03. Apenas usuários autenticados podem solicitar participação em torneios.

RN04. Para solicitar participação em um torneio, o usuário deve possuir ao menos um time previamente cadastrado no sistema.

RN05. Um torneio pode ser criado já com todos os times participantes definidos ou pode ser criado com vagas abertas para solicitações de participação.

RN06. Apenas o organizador do torneio pode aprovar ou rejeitar solicitações de participação de times.

---

## Organização do torneio

RN07. Todo torneio deve possuir um formato definido.

RN08. Um torneio pode ser criado nos formatos mata-mata, fase de grupos com mata-mata, pontos corridos ou final única.

RN09. Todo torneio deve possuir um organizador responsável.

RN10. Um torneio só pode ser iniciado quando possuir a quantidade mínima de times exigida pelo formato da competição.

RN11. A estrutura da competição deve ser gerada de acordo com o formato definido para o torneio.

RN12. Todo torneio deve definir a quantidade de jogadores por equipe em campo.

RN13. As partidas do torneio devem respeitar a quantidade de jogadores definida no formato de equipe.

---

## Times, jogadores e técnicos

RN14. Um time só pode participar de um torneio se estiver devidamente inscrito ou aprovado nele.

RN15. Um jogador só pode participar de partidas por um time ao qual esteja vinculado.

RN16. Técnicos devem estar associados aos times do torneio.

RN17. Apenas jogadores vinculados aos times participantes podem ter eventos registrados em partidas do torneio.

---

## Partidas e andamento da competição

RN18. Toda partida deve estar associada a um torneio e a dois times participantes desse torneio.

RN19. Apenas partidas registradas dentro de um torneio podem influenciar classificação, chaveamento, artilharia e estatísticas.

RN20. Ao registrar o resultado de uma partida, o sistema deve atualizar automaticamente a classificação ou o avanço no chaveamento, conforme o formato do torneio.

RN21. O sistema não deve permitir registrar resultado para uma partida inexistente ou para times que não pertencem ao torneio.

---

## Estatísticas e nota dos jogadores

RN22. O sistema deve permitir registrar gols, assistências, cartões amarelos e cartões vermelhos dos jogadores em cada partida.

RN23. A nota estatística de cada jogador deve ser calculada automaticamente a partir dos eventos registrados na partida.

RN24. A nota estatística do jogador deve ser calculada por meio de uma fórmula baseada em pesos associados aos eventos registrados.

RN25. Na versão inicial do sistema, a fórmula da nota estatística deve considerar gols, assistências, cartões amarelos e cartões vermelhos.

RN26. Eventos positivos devem aumentar a nota estatística do jogador, enquanto eventos negativos devem reduzi-la.

RN27. O sistema deve atualizar automaticamente a artilharia do torneio com base nos gols registrados.