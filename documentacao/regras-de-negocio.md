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

RN08A. Um novo palpite autenticado concede pontos de participacao, com bonus apenas na primeira participacao do dia; alterar a opcao de um palpite existente nao concede novos pontos.

RN08B. A apuracao concede pontos uma unica vez, com recompensa maior para acerto, e atualiza automaticamente nivel, sequencia, selos e ranking do usuario.

RN09. Palpites apurados nao podem ser alterados nem removidos.

RN10. Palpites de visitantes tambem devem ser salvos para contagem, percentuais e apuracao.

RN11. O usuario pode cadastrar uma conta informando nome, email, senha validos e tipo de conta.

RN12. O email da conta de usuario deve ser unico na plataforma.

RN13. O login deve autenticar o usuario apenas quando email e senha forem validos.

RN14. O usuario pode editar os dados cadastrais da propria conta.

RN15. O usuario pode excluir a propria conta, removendo seu acesso ao sistema.

RN16. A conta de usuario pode ser comum, sem funcao esportiva, ou representar jogador, treinador, membro de comissao ou organizador.

RN16A. A conta comum pode usar recursos sociais, palpites e acompanhamento, mas nao pode criar torneios, gerenciar times ou possuir perfil profissional esportivo.

RN17. Contas do tipo jogador podem usar a plataforma para buscar times e acompanhar oportunidades de participacao.

RN18. Contas do tipo organizador gerenciam exclusivamente torneios; somente contas de treinador podem criar e administrar times e elencos.

RN19. Apenas usuarios autenticados podem solicitar conversas privadas no chat.

RN19A. Um grupo deve possuir pelo menos duas pessoas entre criador, participantes liberados e convidados pendentes.

RN19B. Usuarios com conversa privada aprovada entram imediatamente no grupo. Outros usuarios recebem convite e so acessam as mensagens apos aceitar.

RN19C. O treinador pode incluir diretamente jogadores e membros da comissao vinculados aos times sob sua responsabilidade, mesmo sem conversa privada anterior.

RN19D. Publicacoes, comentarios e perfis podem ser denunciados por usuarios autenticados; denuncias pendentes duplicadas do mesmo usuario para o mesmo alvo sao bloqueadas e ficam disponiveis para analise de moderacao.

RN19E. Toda publicacao manual guarda a conta autora real e pode ser exibida como usuario, time ou torneio quando a conta possuir permissao sobre a identidade escolhida.

RN19F. O responsavel pode publicar representando seus times e o organizador pode publicar representando seus torneios; nenhuma conta pode representar entidade administrada por outra pessoa.

RN19G. Postagens podem conter texto, emojis, uma ou mais fotos, videos ou combinacoes desses conteudos; comentarios podem conter texto, foto ou ambos, mas nenhum conteudo pode ser totalmente vazio.

RN19H. Cada conta possui no maximo uma curtida por publicacao; clicar novamente remove a propria curtida, sem criar duplicidade.

RN19I. Comentarios pertencem a uma publicacao principal e nao devem aparecer como posts independentes na timeline.

RN19J. O perfil publico exibe em aba separada e discreta somente as postagens pessoais da conta, usando o mesmo nome e a mesma foto do perfil.

RN19JA. Postagens publicadas representando um time ou torneio aparecem apenas na aba de publicacoes da entidade representada e nunca no historico pessoal do responsavel.

RN19K. Assuntos do momento sao calculados a partir das hashtags e do engajamento de curtidas, reacoes e comentarios.

RN19L. Uma publicacao ativa pode ser encaminhada para conversas privadas aprovadas ou grupos acessiveis ao usuario; a mensagem guarda a referencia da publicacao e o chat exibe seu conteudo atualizado em um card.

RN19M. Postagens do feed podem conter ate quatro imagens ou videos, respeitando o limite de tamanho definido pela interface.

RN19N. A lateral do feed apresenta um radar com as atividades recentes de times e torneios para conectar o usuario ao que esta acontecendo na comunidade, sem expor a fila administrativa de moderacao.

RN20. Uma solicitacao de conversa deve ser salva como solicitada e exibida na aba de solicitados do destinatario.

RN21. O destinatario pode aprovar ou recusar uma solicitacao de conversa recebida.

RN22. Mensagens privadas so podem ser enviadas depois que a conversa for aprovada.

RN23. Apenas participantes da conversa podem enviar mensagens e consultar a conversa aprovada.

RN24. Usuarios nao autenticados nao podem solicitar conversa, aprovar pedidos nem enviar mensagens.

RN25. O sistema deve impedir nova solicitacao quando ja existir conversa solicitada ou aprovada entre os mesmos usuarios.

---

## Participacao no torneio

RN26. Apenas usuarios autenticados podem gerenciar inscricoes de participacao em torneios.

RN27. Para enviar candidatura de participacao em um torneio, o treinador deve possuir ao menos um time previamente cadastrado no sistema.

RN28. Um torneio pode ser criado ja com todos os times participantes definidos ou com vagas abertas para solicitacoes de participacao.

RN29. O usuario pode acompanhar o status das candidaturas de participacao enviadas por ele.

RN30. O usuario pode cancelar uma candidatura de participacao enquanto ela estiver pendente.

RN31. Candidaturas ja avaliadas pelo organizador nao podem ser canceladas pelo solicitante.

RN32. Apenas o organizador do torneio pode aprovar, rejeitar e ajustar a lista final de participantes antes do inicio do torneio.

RN33. Um time so pode participar de um torneio se estiver devidamente inscrito ou aprovado nele.

---

## Times, elenco e desempenho

RN34. Apenas a conta de treinador responsavel pelo time pode cadastrar, editar, excluir ou consultar informacoes protegidas desse time.

RN35. Um time vinculado a torneio nao pode ser excluido enquanto o vinculo impedir a remocao segura.

RN36. Um jogador so pode participar de partidas por um time ao qual esteja vinculado.

RN37. Tecnicos devem estar associados aos times participantes do torneio.

RN38. Apenas jogadores vinculados aos times participantes podem ter eventos registrados em partidas do torneio.

RN39. Comparativos de desempenho podem ser gerados temporariamente entre times ou jogadores usando estatisticas, historico de partidas e rankings.

RN40. Comparativos temporarios nao sao salvos automaticamente.

RN41. O usuario pode salvar um comparativo escolhido para consulta posterior.

RN42. O usuario pode consultar comparativos gerais salvos.

RN43. O usuario pode atualizar um comparativo salvo quando os dados estatisticos mudarem.

RN44. O usuario pode excluir um comparativo salvo do historico.

RN45. O comparativo geral deve ser exibido mesmo quando um perfil ainda nao possui scout, apresentando os indicadores ausentes com valor zero.

RN45A. O comparativo pode ser exportado em CSV ou compartilhado por um link que preserve os dois jogadores ou times selecionados e reabra a analise com os dados atualizados.

RN46. A escalacao da partida e opcional e pode ser informada como lista de titulares, lista de titulares e reservas ou mesa tatica.

RN47. Somente o tecnico responsavel pelo time pode criar ou editar sua escalacao.

RN48. A mesa tatica deve indicar um esquema compativel com o formato 3x3, 5x5, 7x7 ou 11x11 da partida.

RN49. A quantidade de titulares, em lista ou mesa, deve ser exatamente igual ao formato de equipe da partida.

RN50. Na mesa tatica, cada titular deve ocupar uma posicao do esquema e as coordenadas devem distribuir dinamicamente os jogadores de cada linha sem sobreposicao.

RN51. Todos os titulares e reservas devem pertencer ao elenco do time.

RN52. Reservas sao opcionais; o modo somente titulares nao exige nem armazena reservas.

RN53. O mesmo jogador nao pode aparecer simultaneamente como titular e reserva da mesma escalacao.

RN54. A escalacao fica privada e editavel ate o inicio da partida, quando e congelada e liberada para visualizacao publica.

RN55. A ausencia de escalacao nao impede o inicio da partida, o andamento da competicao nem o registro de eventos estatisticos.

RN56. Duas mesas taticas so sao exibidas lado a lado quando ambos os times escolherem esse modo; se os modos forem diferentes ou apenas um time informar a escalacao, a exibicao publica usa listas.

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

RN64A. Apenas uma conta organizadora pode criar torneios e somente o organizador responsavel pode configurar o proprio torneio.

RN64B. Formato da competicao, quantidade de jogadores por equipe, organizador responsavel e numero da edicao sao imutaveis depois da criacao.

RN64C. Nome, regra de entrada, participantes e modo de preparacao podem ser ajustados pelo organizador enquanto o torneio nao tiver iniciado.

RN64D. A preparacao pode ser automatica por sorteio ou manual pela ordem dos participantes e deve gerar estrutura, rodadas e partidas em um unico fluxo.

RN65. O uso de desafios e amistosos e opcional e nao impede o funcionamento dos torneios oficiais.

RN66. Apenas o treinador responsavel por um time pode propor desafio amistoso para outro time.

RN67. Um time nao pode desafiar ele mesmo.

RN68. O responsavel pelo time desafiado pode aceitar ou recusar o convite de amistoso.

RN69. Responsaveis pelos times envolvidos podem reagendar data e local do amistoso antes do encerramento.

RN70. Responsaveis pelos times envolvidos podem registrar o resultado de um amistoso aceito.

RN71. O resultado do amistoso deve ficar disponivel no historico dos dois times envolvidos.

RN71A. Apenas contas do tipo treinador com pelo menos um time sob sua responsabilidade podem visualizar e executar a acao de solicitar confronto amistoso.

RN71B. Quando o treinador possuir apenas um time, esse time deve ser selecionado automaticamente como proponente; quando possuir mais de um, ele deve escolher qual time enviara o desafio.

RN71C. O perfil administrado do time deve separar confrontos recebidos, enviados, confirmados e encerrados, permitindo as acoes compativeis com cada estado.

RN71D. Nao pode existir mais de um desafio proposto ou aceito simultaneamente entre os mesmos dois times.

RN71E. Ao propor um desafio, o responsavel pelo time desafiado deve ser notificado; ao aceitar, os responsaveis dos dois times devem receber a confirmacao do amistoso.

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

RN87A. A visualizacao publica do torneio deve exibir Classificacao somente nos formatos que utilizam tabela e Fase eliminatoria somente nos formatos que utilizam chaveamento; torneios mistos exibem as duas areas em abas separadas.

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

---

## Central de notificacoes

RN101. Toda notificacao deve ser persistida com destinatario, categoria, titulo, mensagem, link, data de criacao e estados de leitura e arquivamento.

RN102. Apenas o usuario destinatario pode marcar ou arquivar uma notificacao.

RN103. Marcar uma notificacao como lida nao deve remove-la do historico.

RN104. Arquivar uma notificacao deve remove-la da lista ativa, preservando-a no historico arquivado e marcando-a como lida.

RN105. O usuario pode marcar todas as suas notificacoes ativas como lidas em uma unica operacao.

RN106. As preferencias de categorias devem ser persistidas por usuario e aplicadas antes da criacao de uma nova notificacao.

RN107. Quando nao houver preferencias salvas, todas as categorias de notificacao devem estar habilitadas por padrao.

RN108. A central deve permitir consultar avisos das categorias torneio, time, amistoso, social e sistema.
