# Regras de Negocio

## Conta, comunicacao e engajamento inicial

RN01. Usuarios autenticados e visitantes identificados podem registrar palpites sobre eventos do sistema.

RN02. O sistema deve permitir palpites sobre vencedor de partida, campeao do torneio, artilheiro e lider de assistencias.

RN03. Cada votante identificado, seja usuario autenticado ou visitante, pode registrar no maximo um palpite por evento alvo.

RN04. O usuario pode alterar seu palpite enquanto a janela de votacao do evento estiver aberta.

RN05. A janela de votacao do palpite de vencedor de partida fecha no inicio da partida.

RN06. A janela de votacao dos palpites de campeao, artilheiro e lider de assistencias fecha no inicio do torneio.

RN07. O sistema deve exibir o percentual de votos por opcao para todo palpite com janela aberta.

RN08. Apos o evento alvo ser concluido, o sistema deve apurar automaticamente o resultado real e marcar cada palpite como acertado ou nao acertado.

RN09. Palpites apurados nao podem ser alterados nem removidos.

RN10. Palpites de visitantes tambem devem ser salvos para contagem, percentuais e apuracao.

RN11. O usuario pode cadastrar uma conta informando nome, email, senha validos e tipo de conta.

RN12. O email da conta de usuario deve ser unico na plataforma.

RN13. O login deve autenticar o usuario apenas quando email e senha forem validos.

RN14. O usuario pode editar os dados cadastrais da propria conta.

RN15. O usuario pode excluir a propria conta, removendo seu acesso ao sistema.

RN16. A conta de usuario pode ser do tipo jogador ou organizador.

RN17. Contas do tipo jogador podem usar a plataforma para buscar times e acompanhar oportunidades de participacao.

RN18. Contas do tipo organizador podem representar responsaveis por times ou organizadores de torneios.

RN19. Apenas usuarios autenticados podem solicitar conversas privadas no chat.

RN20. Uma solicitacao de conversa deve ser salva como solicitada e exibida na aba de solicitados do destinatario.

RN21. O destinatario pode aprovar ou recusar uma solicitacao de conversa recebida.

RN22. Mensagens privadas so podem ser enviadas depois que a conversa for aprovada.

RN23. Apenas participantes da conversa podem enviar mensagens e consultar a conversa aprovada.

RN24. Usuarios nao autenticados nao podem solicitar conversa, aprovar pedidos nem enviar mensagens.

RN25. O sistema deve impedir nova solicitacao quando ja existir conversa solicitada ou aprovada entre os mesmos usuarios.

---

## Participacao no torneio

RN26. Apenas usuarios autenticados podem gerenciar inscricoes de participacao em torneios.

RN27. Para enviar candidatura de participacao em um torneio, o usuario deve possuir ao menos um time previamente cadastrado no sistema.

RN28. Um torneio pode ser criado ja com todos os times participantes definidos ou com vagas abertas para solicitacoes de participacao.

RN29. O usuario pode acompanhar o status das candidaturas de participacao enviadas por ele.

RN30. O usuario pode cancelar uma candidatura de participacao enquanto ela estiver pendente.

RN31. Candidaturas ja avaliadas pelo organizador nao podem ser canceladas pelo solicitante.

RN32. Apenas o organizador do torneio pode aprovar, rejeitar e ajustar a lista final de participantes antes do inicio do torneio.

RN33. Um time so pode participar de um torneio se estiver devidamente inscrito ou aprovado nele.

---

## Times, elenco e desempenho

RN34. Apenas usuario autenticado responsavel pelo time pode cadastrar, editar, excluir ou consultar informacoes protegidas desse time.

RN35. Um time vinculado a torneio nao pode ser excluido enquanto o vinculo impedir a remocao segura.

RN36. Um jogador so pode participar de partidas por um time ao qual esteja vinculado.

RN37. Tecnicos devem estar associados aos times participantes do torneio.

RN38. Apenas jogadores vinculados aos times participantes podem ter eventos registrados em partidas do torneio.

RN39. Comparativos de desempenho podem ser gerados temporariamente entre times ou jogadores usando estatisticas, historico de partidas e rankings.

RN40. Comparativos temporarios nao sao salvos automaticamente.

RN41. O usuario pode salvar um comparativo escolhido para consulta posterior.

RN42. O usuario pode consultar comparativos salvos de um torneio.

RN43. O usuario pode atualizar um comparativo salvo quando os dados estatisticos mudarem.

RN44. O usuario pode excluir um comparativo salvo do historico.

RN45. O sistema deve impedir a geracao de comparativo quando nao houver dados estatisticos suficientes para comparar os envolvidos.

RN46. A mesa tatica da partida e sempre opcional e funciona apenas como visualizacao da escalacao do time em campo.

RN47. A mesa tatica do time pode ser gerada pelo usuario responsavel pelo time ou pelo tecnico associado ao time.

RN48. A mesa tatica deve indicar um esquema tatico compativel com o formato de equipe do torneio.

RN49. A quantidade de jogadores titulares na mesa tatica deve ser exatamente igual ao formato de equipe do torneio.

RN50. Cada titular da mesa tatica deve estar associado a uma posicao definida pelo esquema tatico escolhido e a um posicionamento em campo.

RN51. Os jogadores titulares e reservas da mesa tatica devem pertencer ao elenco do time.

RN52. Nao ha limite maximo de jogadores reservas na mesa tatica.

RN53. O mesmo jogador nao pode aparecer simultaneamente como titular e reserva da mesma mesa tatica.

RN54. A mesa tatica pode ser criada e editada ate o inicio da partida e fica congelada apos esse momento.

RN55. A ausencia de mesa tatica nao impede o inicio da partida, o andamento da competicao nem o registro de eventos estatisticos.

RN56. Um time pode gerar mesa tatica mesmo que o outro nao gere, pois essa visualizacao nao altera as regras da partida.

---

## Organizacao e comunicacao

RN57. Apenas usuarios autenticados podem criar torneios.

RN58. Todo torneio deve possuir um formato definido.

RN59. Um torneio pode ser criado nos formatos mata-mata, fase de grupos com mata-mata, pontos corridos ou final unica.

RN60. Todo torneio deve possuir um organizador responsavel.

RN61. Um torneio so pode ser iniciado quando possuir a quantidade minima de times exigida pelo formato da competicao.

RN62. A preparacao da competicao deve gerar estrutura, rodadas e partidas de acordo com o formato definido para o torneio.

RN63. Todo torneio deve definir a quantidade de jogadores por equipe em campo.

RN64. As partidas do torneio devem respeitar a quantidade de jogadores definida no formato de equipe.

RN65. O uso de desafios e amistosos e opcional e nao impede o funcionamento dos torneios oficiais.

RN66. Apenas usuario autenticado responsavel por um time pode propor desafio amistoso para outro time.

RN67. Um time nao pode desafiar ele mesmo.

RN68. O responsavel pelo time desafiado pode aceitar ou recusar o convite de amistoso.

RN69. Responsaveis pelos times envolvidos podem reagendar data e local do amistoso antes do encerramento.

RN70. Responsaveis pelos times envolvidos podem registrar o resultado de um amistoso aceito.

RN71. O resultado do amistoso deve ficar disponivel no historico dos dois times envolvidos.

RN72. Apenas o organizador do torneio pode publicar comunicados oficiais no feed social do torneio.

RN73. Usuarios autenticados podem comentar em partidas pertencentes ao torneio.

RN74. Usuarios nao autenticados nao podem comentar no feed social do torneio.

RN75. O sistema pode publicar atualizacoes automaticas no feed quando houver eventos relevantes da partida, como resultado registrado.

RN76. Comentarios do feed podem ser editados pelo proprio autor.

RN77. O feed social geral pode ser visualizado por visitantes, mas apenas usuarios autenticados podem publicar, comentar, curtir ou reagir.

RN78. Publicacoes do feed social podem conter texto, midias e hashtags.

RN79. O feed social pode ser filtrado por hashtag para acompanhar assuntos, torneios, times ou peladas especificas.

RN80. A estrutura e a preparacao da competicao podem ser feitas por sorteio automatico ou por montagem manual do organizador.

RN81. A montagem manual deve utilizar apenas times aprovados no torneio.

RN82. Um torneio finalizado pode ser repetido como nova edicao, mantendo o historico da edicao anterior.

RN83. Ao repetir um torneio, os participantes da nova edicao devem ser definidos novamente.

RN84. As estatisticas da edicao anterior devem permanecer arquivadas antes de reiniciar o ciclo estatistico da nova edicao.

---

## Partidas, andamento e estatisticas

RN85. Toda partida deve estar associada a um torneio e a dois times participantes desse torneio.

RN86. Apenas partidas registradas dentro de um torneio podem influenciar classificacao, chaveamento, artilharia e estatisticas.

RN87. Ao registrar o resultado de uma partida, o sistema deve atualizar automaticamente a classificacao, o avanco no chaveamento e o status da partida, conforme o formato do torneio.

RN88. O sistema nao deve permitir registrar resultado para uma partida inexistente ou para times que nao pertencem ao torneio.

RN89. O resultado da partida pode ser registrado independentemente do registro de eventos estatisticos.

RN90. O sistema pode registrar gols, assistências, cartões amarelos, cartões vermelhos, faltas e substituições no scout opcional de cada partida, vinculando cada evento a um jogador da escalação confirmada e ao minuto da partida.

RN91. Eventos de scout só podem ser inseridos, editados ou removidos enquanto a partida estiver em andamento ou em até 48 horas após ser marcada como finalizada, após esse prazo a súmula torna-se somente leitura de forma irreversível.

RN92. Ao registrar um segundo cartão amarelo para um jogador na mesma partida, o sistema deve converter automaticamente o evento para cartão vermelho e marcar o jogador como expulso, impedindo novos eventos para ele naquela partida.

RN93. O sistema deve validar substituições com base em duas dimensões simultâneas: o total de trocas do time não pode exceder o limite configurado no torneio, e as substituições devem ocorrer em no máximo o número de paradas de jogo permitidas pela configuração do torneio.

RN94. Ao remover ou corrigir qualquer evento de scout, o sistema deve executar uma transação atômica que reverte o placar, os contadores estatísticos do jogador e o status dele em campo, realizando rollback completo em caso de falha em qualquer etapa.

RN95. O sistema deve verificar o acúmulo de cartões amarelos do jogador em toda a edição do torneio e gerar automaticamente uma suspensão para a próxima partida quando o total atingir o múltiplo do limite configurado.

RN96. A consolidação das estatísticas deve atualizar artilharia, líderes de assistências e histórico dos jogadores de forma incremental a cada evento registrado, sem recalcular todos os eventos anteriores do zero.

RN97. A artilharia deve ser ordenada com critérios de desempate em cascata: maior número de gols, depois mais assistências, depois menos cartões vermelhos, depois menos cartões amarelos, depois mais minutos jogados.

RN98. Para figurar no ranking de melhor avaliado da edição, o jogador deve ter participado em no mínimo 30% das partidas finalizadas realizadas pelo seu time na edição.

RN99. Quando um resultado ou evento de scout for corrigido após o fechamento de uma partida, o sistema deve executar um recálculo retroativo completo da edição, zerando e reprocessando toda a classificação e os scouts consolidados de todos os jogadores afetados, registrando log de auditoria da operação.

RN100. Quando todas as partidas de uma edição estiverem finalizadas, o organizador pode encerrar a edição, momento em que o sistema registra o campeão, vice e destaques de forma imutável no histórico da liga.
