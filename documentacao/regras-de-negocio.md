# Regras de Negocio

## Acesso e participacao

RN01. Usuarios nao autenticados podem visualizar os torneios disponiveis na pagina inicial do sistema.

RN02. Apenas usuarios autenticados podem criar torneios.

RN03. Apenas usuarios autenticados podem solicitar participacao em torneios.

RN04. Para solicitar participacao em um torneio, o usuario deve possuir ao menos um time previamente cadastrado no sistema.

RN05. Um torneio pode ser criado ja com todos os times participantes definidos ou pode ser criado com vagas abertas para solicitacoes de participacao.

RN06. Apenas o organizador do torneio pode aprovar ou rejeitar solicitacoes de participacao de times.

---

## Organizacao do torneio

RN07. Todo torneio deve possuir um formato definido.

RN08. Um torneio pode ser criado nos formatos mata-mata, fase de grupos com mata-mata, pontos corridos ou final unica.

RN09. Todo torneio deve possuir um organizador responsavel.

RN10. Um torneio so pode ser iniciado quando possuir a quantidade minima de times exigida pelo formato da competicao.

RN11. A estrutura da competicao deve ser gerada de acordo com o formato definido para o torneio.

RN12. Todo torneio deve definir a quantidade de jogadores por equipe em campo.

RN13. As partidas do torneio devem respeitar a quantidade de jogadores definida no formato de equipe.

---

## Times, jogadores e tecnicos

RN14. Um time so pode participar de um torneio se estiver devidamente inscrito ou aprovado nele.

RN15. Um jogador so pode participar de partidas por um time ao qual esteja vinculado.

RN16. Tecnicos devem estar associados aos times participantes do torneio.

RN17. Apenas jogadores vinculados aos times participantes podem ter eventos registrados em partidas do torneio.

---

## Partidas e andamento da competicao

RN18. Toda partida deve estar associada a um torneio e a dois times participantes desse torneio.

RN19. Apenas partidas registradas dentro de um torneio podem influenciar classificacao, chaveamento, artilharia e estatisticas.

RN20. Ao registrar o resultado de uma partida, o sistema deve atualizar automaticamente a classificacao ou o avanco no chaveamento, conforme o formato do torneio.

RN21. O sistema nao deve permitir registrar resultado para uma partida inexistente ou para times que nao pertencem ao torneio.

RN22. O resultado da partida pode ser registrado independentemente do registro de eventos estatisticos.

---

## Estatisticas e nota dos jogadores

RN23. O sistema pode registrar gols, assistencias, cartoes amarelos e cartoes vermelhos dos jogadores em cada partida.

RN24. A nota estatistica de cada jogador deve ser calculada automaticamente a partir dos eventos registrados na partida, quando esses eventos existirem.

RN25. A nota estatistica do jogador deve ser calculada por meio de uma formula baseada em pesos associados aos eventos registrados.

RN26. Na versao inicial do sistema, a formula da nota estatistica deve considerar gols, assistencias, cartoes amarelos e cartoes vermelhos.

RN27. Eventos positivos devem aumentar a nota estatistica do jogador, enquanto eventos negativos devem reduzi-la.

RN28. O sistema deve atualizar automaticamente a artilharia do torneio com base nos gols registrados, quando houver registro de eventos.

RN29. Na ausencia de eventos registrados, o sistema deve manter disponivel apenas o placar oficial da partida, sem exibir nota estatistica, artilharia ou estatisticas detalhadas.
