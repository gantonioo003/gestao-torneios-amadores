# Regras de Negocio

## Conta e engajamento inicial

RN01. Usuarios autenticados e visitantes identificados podem registrar palpites publicos sobre eventos do sistema.

RN02. O sistema deve permitir palpites sobre vencedor de partida, campeao do torneio, artilheiro e lider de assistencias.

RN03. Cada votante identificado, seja usuario autenticado ou visitante, pode registrar no maximo um palpite por evento alvo.

RN04. O usuario pode alterar seu palpite enquanto a janela de votacao do evento estiver aberta.

RN05. A janela de votacao do palpite de vencedor de partida fecha no inicio da partida.

RN06. A janela de votacao dos palpites de campeao, artilheiro e lider de assistencias fecha no inicio do torneio.

RN07. O sistema deve exibir o percentual de votos por opcao para todo palpite com janela aberta.

RN08. Apos o evento alvo ser concluido, o sistema deve apurar automaticamente o resultado real e marcar cada palpite como acertado ou nao acertado.

RN09. Palpites apurados nao podem ser alterados nem removidos.

RN10. Palpites de visitantes tambem devem ser salvos para contagem, percentuais e apuracao.

RN11. O usuario pode cadastrar uma conta informando nome, email e senha validos.

RN12. O email da conta de usuario deve ser unico na plataforma.

RN13. O login deve autenticar o usuario apenas quando email e senha forem validos.

RN14. O usuario pode editar os dados cadastrais da propria conta.

RN15. O usuario pode excluir a propria conta, removendo seu acesso ao sistema.

---

## Participacao no torneio

RN16. Apenas usuarios autenticados podem gerenciar candidaturas de participacao em torneios.

RN17. Para enviar candidatura de participacao em um torneio, o usuario deve possuir ao menos um time previamente cadastrado no sistema.

RN18. Um torneio pode ser criado ja com todos os times participantes definidos ou com vagas abertas para solicitacoes de participacao.

RN19. O usuario pode acompanhar o status das candidaturas de participacao enviadas por ele.

RN20. O usuario pode cancelar uma candidatura de participacao enquanto ela estiver pendente.

RN21. Candidaturas ja avaliadas pelo organizador nao podem ser canceladas pelo solicitante.

RN22. Apenas o organizador do torneio pode aprovar, rejeitar e ajustar a lista final de participantes antes do inicio do torneio.

RN23. Um time so pode participar de um torneio se estiver devidamente inscrito ou aprovado nele.

---

## Times, elenco e desempenho

RN24. Apenas usuario autenticado responsavel pelo time pode cadastrar, editar, excluir ou consultar informacoes protegidas desse time.

RN25. Um time vinculado a torneio nao pode ser excluido enquanto o vinculo impedir a remocao segura.

RN26. Um jogador so pode participar de partidas por um time ao qual esteja vinculado.

RN27. Tecnicos devem estar associados aos times participantes do torneio.

RN28. Apenas jogadores vinculados aos times participantes podem ter eventos registrados em partidas do torneio.

RN29. Comparativos de desempenho podem ser gerados temporariamente entre times ou jogadores usando estatisticas, historico de partidas e rankings.

RN30. Comparativos temporarios nao sao salvos automaticamente.

RN31. O usuario pode salvar um comparativo escolhido para consulta posterior.

RN32. O usuario pode consultar comparativos salvos de um torneio.

RN33. O usuario pode atualizar um comparativo salvo quando os dados estatisticos mudarem.

RN34. O usuario pode excluir um comparativo salvo do historico.

RN35. O sistema deve impedir a geracao de comparativo quando nao houver dados estatisticos suficientes para comparar os envolvidos.

RN36. A escalacao da partida e opcional quando o torneio ou a partida nao exigirem esse detalhamento.

RN37. A escalacao do time pode ser definida pelo usuario responsavel pelo time ou pelo tecnico associado ao time.

RN38. A escalacao deve indicar um esquema tatico compativel com o formato de equipe do torneio.

RN39. A quantidade de jogadores titulares na escalacao deve ser exatamente igual ao formato de equipe do torneio.

RN40. Cada titular da escalacao deve estar associado a uma posicao definida pelo esquema tatico escolhido.

RN41. Os jogadores titulares e reservas da escalacao devem pertencer ao elenco do time.

RN42. Nao ha limite maximo de jogadores reservas na escalacao.

RN43. O mesmo jogador nao pode aparecer simultaneamente como titular e reserva da mesma escalacao.

RN44. A escalacao pode ser criada e editada ate o inicio da partida e fica congelada apos esse momento.

RN45. Se a partida ou o torneio exigir escalacao, os dois times devem informar escalacao antes do inicio.

RN46. Se um time informar escalacao em uma partida opcional, o outro time tambem deve informar escalacao para manter equilibrio nos dados da partida.

---

## Organizacao e comunicacao

RN47. Apenas usuarios autenticados podem criar torneios.

RN48. Todo torneio deve possuir um formato definido.

RN49. Um torneio pode ser criado nos formatos mata-mata, fase de grupos com mata-mata, pontos corridos ou final unica.

RN50. Todo torneio deve possuir um organizador responsavel.

RN51. Um torneio so pode ser iniciado quando possuir a quantidade minima de times exigida pelo formato da competicao.

RN52. A preparacao da competicao deve gerar estrutura, rodadas e partidas de acordo com o formato definido para o torneio.

RN53. Todo torneio deve definir a quantidade de jogadores por equipe em campo.

RN54. As partidas do torneio devem respeitar a quantidade de jogadores definida no formato de equipe.

RN55. O uso de desafios e amistosos e opcional e nao impede o funcionamento dos torneios oficiais.

RN56. Apenas usuario autenticado responsavel por um time pode propor desafio amistoso para outro time.

RN57. Um time nao pode desafiar ele mesmo.

RN58. O responsavel pelo time desafiado pode aceitar ou recusar o convite de amistoso.

RN59. Responsaveis pelos times envolvidos podem reagendar data e local do amistoso antes do encerramento.

RN60. Responsaveis pelos times envolvidos podem registrar o resultado de um amistoso aceito.

RN61. O resultado do amistoso deve ficar disponivel no historico dos dois times envolvidos.

RN62. Apenas o organizador do torneio pode publicar comunicados oficiais no feed social do torneio.

RN63. Usuarios autenticados podem comentar em partidas pertencentes ao torneio.

RN64. Usuarios nao autenticados nao podem comentar no feed social do torneio.

RN65. O sistema pode publicar atualizacoes automaticas no feed quando houver eventos relevantes da partida, como resultado registrado.

RN66. Comentarios do feed podem ser editados pelo proprio autor.

---

## Partidas, andamento e estatisticas

RN67. Toda partida deve estar associada a um torneio e a dois times participantes desse torneio.

RN68. Apenas partidas registradas dentro de um torneio podem influenciar classificacao, chaveamento, artilharia e estatisticas.

RN69. Ao registrar o resultado de uma partida, o sistema deve atualizar automaticamente a classificacao, o avanco no chaveamento e o status da partida, conforme o formato do torneio.

RN70. O sistema nao deve permitir registrar resultado para uma partida inexistente ou para times que nao pertencem ao torneio.

RN71. O resultado da partida pode ser registrado independentemente do registro de eventos estatisticos.

RN72. O sistema pode registrar gols, assistencias, cartoes amarelos, cartoes vermelhos e substituicoes dos jogadores em cada partida.

RN73. A nota estatistica de cada jogador deve ser calculada automaticamente a partir dos eventos registrados na partida, quando esses eventos existirem.

RN74. A nota estatistica do jogador deve ser calculada por meio de uma formula baseada em pesos associados aos eventos registrados.

RN75. Na versao inicial do sistema, a formula da nota estatistica deve considerar gols, assistencias, cartoes amarelos e cartoes vermelhos.

RN76. Eventos positivos devem aumentar a nota estatistica do jogador, enquanto eventos negativos devem reduzi-la.

RN77. O sistema deve atualizar automaticamente a artilharia do torneio com base nos gols registrados, quando houver registro de eventos.

RN78. Na ausencia de eventos registrados, o sistema deve manter disponivel apenas o placar oficial da partida, sem exibir nota estatistica, artilharia ou estatisticas detalhadas.

RN79. Eventos da sumula estatistica podem ser corrigidos ou removidos pelo organizador do torneio.

RN80. A consolidacao das estatisticas deve atualizar notas, artilharia, lideres de assistencias e historico dos jogadores a partir dos eventos registrados.

RN81. Substituicoes so podem ser registradas quando a partida possui escalacao, pois dependem de saber quem saiu e quem entrou.
