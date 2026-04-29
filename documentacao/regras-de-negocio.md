# Regras de Negocio

## Acesso e participacao

RN01. Apenas usuarios autenticados podem registrar palpites sobre eventos do sistema.

RN02. Apenas usuarios autenticados podem criar torneios.

RN03. Apenas usuarios autenticados podem gerenciar candidaturas de participacao em torneios.

RN04. Para enviar candidatura de participacao em um torneio, o usuario deve possuir ao menos um time previamente cadastrado no sistema.

RN05. Um torneio pode ser criado ja com todos os times participantes definidos ou pode ser criado com vagas abertas para solicitacoes de participacao.

RN06. Apenas o organizador do torneio pode aprovar, rejeitar e ajustar a lista final de participantes antes do inicio do torneio.

RN54. O usuario pode cadastrar uma conta informando nome, email e senha validos.

RN55. O email da conta de usuario deve ser unico na plataforma.

RN56. O login deve autenticar o usuario apenas quando email e senha forem validos.

RN57. O usuario pode editar os dados cadastrais da propria conta.

RN58. O usuario pode excluir a propria conta, removendo seu acesso ao sistema.

RN65. O usuario pode acompanhar o status das candidaturas de participacao enviadas por ele.

RN66. O usuario pode cancelar uma candidatura de participacao enquanto ela estiver pendente.

RN67. Candidaturas ja avaliadas pelo organizador nao podem ser canceladas pelo solicitante.

---

## Desafios e amistosos

RN59. Apenas usuario autenticado responsavel por um time pode propor desafio amistoso para outro time.

RN60. Um time nao pode desafiar ele mesmo.

RN61. O responsavel pelo time desafiado pode aceitar ou recusar o convite de amistoso.

RN62. Responsaveis pelos times envolvidos podem reagendar data e local do amistoso antes do encerramento.

RN63. Responsaveis pelos times envolvidos podem registrar o resultado de um amistoso aceito.

RN64. O resultado do amistoso deve ficar disponivel no historico dos dois times envolvidos.

---

## Palpites e engajamento

RN30. O sistema deve permitir registrar palpites sobre vencedor de partida, campeao do torneio, artilheiro e lider de assistencias.

RN31. Cada usuario autenticado pode registrar no maximo um palpite por evento alvo.

RN32. O usuario pode alterar seu palpite enquanto a janela de votacao do evento estiver aberta.

RN33. A janela de votacao do palpite de vencedor de partida fecha no inicio da partida.

RN34. A janela de votacao dos palpites de campeao do torneio, artilheiro e lider de assistencias fecha no inicio do torneio.

RN35. O sistema deve exibir o percentual de votos por opcao para todo palpite com janela aberta.

RN36. Apos o evento alvo ser concluido, o sistema deve apurar automaticamente o resultado real e marcar cada palpite como acertado ou nao acertado.

RN37. Palpites apurados nao podem ser alterados nem removidos.

---

## Feed social do torneio

RN48. Apenas o organizador do torneio pode publicar comunicados oficiais no feed social do torneio.

RN49. Usuarios autenticados podem comentar em partidas pertencentes ao torneio.

RN50. Usuarios nao autenticados nao podem comentar no feed social do torneio.

RN51. O sistema pode publicar atualizacoes automaticas no feed quando houver eventos relevantes da partida, como resultado registrado.

RN52. Comentarios do feed podem ser editados pelo proprio autor.

---

## Organizacao do torneio

RN07. Todo torneio deve possuir um formato definido.

RN08. Um torneio pode ser criado nos formatos mata-mata, fase de grupos com mata-mata, pontos corridos ou final unica.

RN09. Todo torneio deve possuir um organizador responsavel.

RN10. Um torneio so pode ser iniciado quando possuir a quantidade minima de times exigida pelo formato da competicao.

RN11. A preparacao da competicao deve gerar estrutura, rodadas e partidas de acordo com o formato definido para o torneio.

RN12. Todo torneio deve definir a quantidade de jogadores por equipe em campo.

RN13. As partidas do torneio devem respeitar a quantidade de jogadores definida no formato de equipe.

---

## Times, jogadores e tecnicos

RN14. Um time so pode participar de um torneio se estiver devidamente inscrito ou aprovado nele.

RN15. Um jogador so pode participar de partidas por um time ao qual esteja vinculado.

RN16. Tecnicos devem estar associados aos times participantes do torneio.

RN17. Apenas jogadores vinculados aos times participantes podem ter eventos registrados em partidas do torneio.

---

## Escalacao da partida

RN38. Toda partida deve possuir uma escalacao definida para cada um dos dois times antes do inicio.

RN39. A escalacao do time pode ser definida pelo usuario responsavel pelo time ou pelo tecnico associado ao time.

RN40. A escalacao deve indicar um esquema tatico compativel com o formato de equipe do torneio.

RN41. A quantidade de jogadores titulares na escalacao deve ser exatamente igual ao formato de equipe do torneio.

RN42. Cada titular da escalacao deve estar associado a uma posicao definida pelo esquema tatico escolhido.

RN43. Os jogadores titulares e reservas da escalacao devem pertencer ao elenco do time.

RN44. Nao ha limite maximo de jogadores reservas na escalacao.

RN45. O mesmo jogador nao pode aparecer simultaneamente como titular e reserva da mesma escalacao.

RN46. A escalacao pode ser criada e editada ate o inicio da partida e fica congelada apos esse momento.

---

## Partidas e andamento da competicao

RN18. Toda partida deve estar associada a um torneio e a dois times participantes desse torneio.

RN19. Apenas partidas registradas dentro de um torneio podem influenciar classificacao, chaveamento, artilharia e estatisticas.

RN20. Ao registrar o resultado de uma partida, o sistema deve atualizar automaticamente a classificacao, o avanco no chaveamento e o status da partida, conforme o formato do torneio.

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

RN47. Eventos da sumula estatistica podem ser corrigidos ou removidos pelo organizador do torneio.

RN53. A consolidacao das estatisticas deve atualizar notas, artilharia, lideres de assistencias e historico dos jogadores a partir dos eventos registrados.
