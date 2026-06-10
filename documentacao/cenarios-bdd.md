# Cenarios BDD

## Funcionalidades com cenarios definidos

### F1. Gerenciar ciclo completo de palpites publicos de usuarios e visitantes
Dominio: dominio-engajamento
Arquivo: dominio-engajamento/src/test/resources/com/torneios/dominio/engajamento/F1-registrar-palpite.feature

Cenarios principais:
- registrar palpite de usuario autenticado sobre vencedor de partida
- registrar palpite de visitante nao autenticado sobre vencedor de partida
- registrar palpite sobre campeao do torneio com sucesso
- registrar palpite sobre artilheiro do torneio com sucesso
- registrar palpite sobre lider de assistencias do torneio com sucesso
- substituir palpite anterior do mesmo usuario para o mesmo evento alvo
- alterar palpite enquanto a janela de votacao estiver aberta
- impedir alteracao de palpite apos o fechamento da janela de votacao
- impedir palpite com opcao invalida para o evento alvo
- exibir percentual atualizado por opcao enquanto a janela estiver aberta
- apurar palpite como acertado quando a opcao escolhida coincide com o resultado real
- apurar palpite como nao acertado quando a opcao escolhida diverge do resultado real
- impedir alteracao de um palpite apos a apuracao

---

### F2. Gerenciar ciclo completo da conta de usuario e autenticacao
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F2-gerenciar-conta-de-usuario-e-autenticacao.feature

Cenarios principais:
- cadastrar nova conta de usuario
- cadastrar conta do tipo jogador
- cadastrar conta do tipo organizador
- realizar login com email e senha validos
- impedir login com senha incorreta
- editar dados da conta
- excluir conta de usuario
- impedir cadastro com email ja utilizado

---

### F3. Gerenciar comunicacao privada com solicitacoes de conversa
Dominio: dominio-engajamento
Arquivo: dominio-engajamento/src/test/resources/com/torneios/dominio/engajamento/F3-gerenciar-chat-privado-com-solicitacoes-de-conversa.feature

Cenarios principais:
- solicitar conversa privada com outro usuario
- aprovar solicitacao de conversa
- recusar solicitacao de conversa
- enviar mensagem em conversa aprovada
- impedir mensagem antes da aprovacao da conversa
- consultar historico de conversas aprovadas
- impedir solicitacao de conversa por usuario nao autenticado

---

### F4. Gerenciar inscricoes, curadoria e participantes finais do torneio
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F4-gerenciar-inscricoes-e-participantes-do-torneio.feature

Cenarios principais:
- enviar candidatura com time cadastrado
- acompanhar status das candidaturas do time
- cancelar candidatura pendente
- impedir cancelamento de candidatura ja avaliada
- aprovar solicitacao e incluir time na lista final
- rejeitar solicitacao de participacao
- remover time aprovado antes do inicio do torneio
- impedir candidatura sem time cadastrado
- impedir candidatura de usuario nao autenticado
- impedir candidatura em torneio fechado
- impedir candidatura duplicada no mesmo torneio
- impedir gerenciamento por usuario que nao e organizador
- impedir alteracao da lista final apos inicio do torneio
- visualizar lista de times candidatos pendentes
- informar ausencia de solicitacoes pendentes para avaliacao

---

### F5. Gerenciar times do usuario, permitindo cadastrar, editar, excluir e consultar torneios vinculados
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F5-gerenciar-times-do-usuario.feature

Cenarios principais:
- criar um novo time com sucesso
- editar informacoes de um time do usuario
- excluir um time sem vinculo em torneio
- impedir exclusao de time vinculado a torneio
- consultar torneios vinculados ao time

---

### F6. Cadastrar profissional esportivo com historico de carreira
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F6-cadastrar-profissional-esportivo.feature

Cenarios principais:
- cadastrar profissional com nome e tipo validos
- impedir cadastro de profissional sem nome
- impedir cadastro de profissional sem tipo
- exigir autenticacao para cadastrar profissional
- editar profissional cadastrado pelo proprio usuario
- impedir edicao por outro usuario
- remover profissional sem vinculo ativo
- impedir remocao por outro usuario
- adicionar registro de carreira com dados validos
- impedir registro de carreira com dados invalidos
- impedir sobreposicao de periodos no historico
- remover registro de carreira existente

---

### F7. Gerenciar analises comparativas de desempenho entre times e jogadores
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/F7-gerenciar-comparativos-de-desempenho.feature

Cenarios principais:
- gerar comparativo temporario entre jogadores
- gerar comparativo temporario entre times
- salvar comparativo escolhido pelo usuario
- consultar comparativos salvos do torneio
- atualizar comparativo salvo apos mudanca nos dados
- excluir comparativo salvo do historico
- impedir comparativo quando nao houver dados estatisticos

---

### F8. Gerenciar a visualizacao opcional da escalacao do time em mesa tatica para uma partida
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F8-gerar-escalacao-em-mesa-tatica.feature

Cenarios principais:
- permitir partida seguir normalmente sem mesa tatica
- permitir que apenas um time gere mesa tatica sem bloquear a partida
- gerar mesa tatica com esquema tatico, titulares por posicao e reservas com sucesso
- permitir geracao da mesa tatica tanto pelo responsavel do time quanto pelo tecnico associado
- impedir geracao de mesa tatica por usuario que nao e responsavel nem tecnico do time
- impedir geracao de mesa tatica com quantidade de titulares diferente do formato de equipe do torneio
- impedir geracao de mesa tatica com esquema tatico incompativel com o formato de equipe
- impedir geracao de mesa tatica com jogador que nao pertence ao elenco do time
- impedir o mesmo jogador como titular e reserva da mesma mesa tatica
- editar mesa tatica enquanto a partida nao foi iniciada
- impedir edicao de mesa tatica apos o inicio da partida
- aceitar quantidade qualquer de reservas, inclusive zero

---

### F9. Gerenciar o ciclo estrutural do torneio e a preparacao da competicao
Dominio: dominio-torneio
Arquivos:
- dominio-torneio/src/test/resources/com/torneios/dominio/torneio/F9-criar-e-configurar-torneio.feature
- dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F9-preparar-competicao-do-torneio.feature

Cenarios principais:
- criar torneio com formato de competicao e formato de equipe validos
- gerar estrutura do torneio por sorteio
- gerar estrutura do torneio por montagem manual
- repetir torneio mantendo historico da edicao anterior
- preparar competicao por pontos corridos
- preparar competicao por sorteio automatico
- preparar competicao com montagem manual dos confrontos
- preparar competicao mata-mata
- preparar competicao com fase de grupos
- impedir criacao de torneio sem formato de competicao
- impedir criacao de torneio sem definicao da quantidade de jogadores por equipe
- definir se o torneio sera aberto para solicitacao ou com participantes definidos
- impedir preparacao sem estrutura previa da competicao

---

### F10. Gerenciar o ciclo social de desafios e amistosos opcionais entre times
Dominio: dominio-engajamento
Arquivo: dominio-engajamento/src/test/resources/com/torneios/dominio/engajamento/F10-gerenciar-desafios-e-amistosos-entre-times.feature

Cenarios principais:
- propor confronto amistoso para outro time
- aceitar convite de amistoso
- recusar convite de amistoso
- reagendar amistoso aceito
- registrar resultado no historico dos times
- impedir desafio contra o proprio time
- impedir aceite por usuario sem responsabilidade pelos times

---

### F11. Gerenciar o ecossistema de feed social da plataforma e do torneio
Dominio: dominio-engajamento
Arquivo: dominio-engajamento/src/test/resources/com/torneios/dominio/engajamento/F11-gerenciar-comunicados-e-feed-social-do-torneio.feature

Cenarios principais:
- publicar postagem no feed social geral com hashtag e midia
- publicar comunicado oficial no feed do torneio
- impedir comunicado oficial por usuario que nao e organizador
- comentar sobre uma partida do torneio
- impedir comentario de usuario nao autenticado
- publicar atualizacao automatica sobre jogo
- editar comentario pelo proprio autor
- listar publicacoes do feed do torneio
- visitante visualizar feed geral sem interagir
- usuario autenticado curtir e reagir em publicacao do feed
- impedir interacao de visitante no feed
- filtrar publicacoes por hashtag

---

### F12. Gerenciar placar oficial da partida e atualizacao do andamento da competicao
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F12-registrar-placar-e-atualizar-andamento-da-competicao.feature

Cenarios principais:
- registrar resultado valido de uma partida
- registrar apenas o placar da partida sem informar eventos estatisticos
- atualizar automaticamente classificacao ou chaveamento apos resultado
- impedir registro por usuario nao organizador
- impedir registro para partida inexistente ou invalida
- atualizar classificacao e status da partida apos resultado
- gerenciar chaveamento em torneio mata-mata
- consultar classificacao em torneio de pontos corridos
- impedir gerenciamento de andamento sem estrutura gerada

---

### F13. Gerenciar o scout estatistico opcional e detalhado da partida
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/F13-gerenciar-scout-estatistico-opcional-da-partida.feature

Cenarios principais:
- manter a partida sem scout detalhado quando o organizador nao quiser registrar eventos individuais
- registrar gol e assistencia em uma partida
- registrar cartoes em uma partida
- registrar substituicao mesmo quando a partida nao possui mesa tatica
- corrigir evento individual do scout
- remover evento individual do scout
- impedir gerenciamento do scout por usuario nao organizador
- impedir registro de eventos para jogador nao pertencente ao time

---

### F14. Gerenciar a consolidacao historica das estatisticas e rankings do torneio
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/F14-consolidar-estatisticas-e-rankings-do-torneio.feature

Cenarios principais:
- consolidar notas, artilharia, assistencias e historico dos jogadores
- gerar ranking de artilharia
- atualizar estatisticas apos novos eventos
- nao consolidar estatisticas detalhadas quando nao houver eventos registrados

---

### F15.

---

## Regras de negocio cobertas

### Conta, comunicacao e engajamento inicial
- RN01. Usuarios autenticados e visitantes identificados podem registrar palpites publicos.
- RN02. Tipos de palpite suportados: vencedor de partida, campeao, artilheiro e lider de assistencias.
- RN03. Cada votante identificado faz no maximo um palpite por evento alvo.
- RN04. Palpite pode ser alterado enquanto a janela estiver aberta.
- RN05. Janela do palpite de vencedor de partida fecha no inicio da partida.
- RN06. Janela dos demais palpites fecha no inicio do torneio.
- RN07. Sistema exibe percentual de votos por opcao em tempo real.
- RN08. Apuracao automatica de acerto apos a conclusao do evento alvo.
- RN09. Palpites apurados sao imutaveis.
- RN10. Palpites de visitantes tambem devem ser salvos para contagem e percentuais.
- RN11. Usuario pode cadastrar conta informando nome, email, senha e tipo de conta.
- RN12. Email de conta de usuario deve ser unico.
- RN13. Login exige email e senha validos.
- RN14. Usuario pode editar os dados da propria conta.
- RN15. Usuario pode excluir a propria conta.
- RN16. A conta de usuario pode ser do tipo jogador ou organizador.
- RN17. Contas do tipo jogador podem usar a plataforma para buscar times e acompanhar oportunidades de participacao.
- RN18. Contas do tipo organizador podem representar responsaveis por times ou organizadores de torneios.
- RN19. Apenas usuarios autenticados podem solicitar conversas privadas.
- RN20. Solicitacao de conversa fica salva na aba de solicitados do destinatario.
- RN21. Destinatario pode aprovar ou recusar uma solicitacao de conversa.
- RN22. Mensagens privadas so podem ser enviadas apos aprovacao da conversa.
- RN23. Apenas participantes podem enviar mensagens e consultar conversas aprovadas.
- RN24. Usuarios nao autenticados nao podem usar o chat.
- RN25. Sistema impede conversa duplicada quando ja existir solicitacao ou conversa aprovada.

### Participacao no torneio
- RN26. Apenas usuarios autenticados podem gerenciar inscricoes de participacao em torneios.
- RN27. Usuario deve possuir time cadastrado para enviar candidatura.
- RN28. Torneio pode ser aberto ou fechado para participacao.
- RN29. Usuario pode acompanhar o status das candidaturas enviadas por ele.
- RN30. Usuario pode cancelar uma candidatura enquanto ela estiver pendente.
- RN31. Candidaturas ja avaliadas pelo organizador nao podem ser canceladas pelo solicitante.
- RN32. Apenas organizador pode aprovar, rejeitar e ajustar a lista final de participantes antes do inicio do torneio.
- RN33. Time so participa se estiver inscrito ou aprovado.

### Times, elenco e desempenho
- RN34. Apenas usuario autenticado responsavel pelo time pode cadastrar, editar, excluir ou consultar informacoes protegidas desse time.
- RN35. Time vinculado a torneio nao pode ser excluido quando o vinculo impedir remocao segura.
- RN36. Jogador pertence a um time.
- RN37. Tecnico associado ao time participante.
- RN38. Apenas jogadores validos podem ter eventos registrados.
- RN39. Comparativos podem ser gerados temporariamente por estatisticas, historico de partidas e rankings.
- RN40. Comparativos temporarios nao sao salvos automaticamente.
- RN41. Usuario pode salvar um comparativo escolhido.
- RN42. Usuario pode consultar comparativos salvos.
- RN43. Usuario pode atualizar comparativo salvo quando os dados mudarem.
- RN44. Usuario pode excluir comparativo salvo.
- RN45. O sistema deve impedir comparativo sem dados estatisticos suficientes.
- RN46. A mesa tatica e sempre opcional e funciona apenas como visualizacao da escalacao do time em campo.
- RN47. A mesa tatica pode ser gerada pelo responsavel do time ou pelo tecnico.
- RN48. O esquema tatico da mesa tatica deve ser compativel com o formato de equipe.
- RN49. A quantidade de titulares da mesa tatica deve ser igual ao formato de equipe.
- RN50. Cada titular da mesa tatica deve estar associado a uma posicao do esquema e a um posicionamento em campo.
- RN51. Titulares e reservas da mesa tatica devem pertencer ao elenco do time.
- RN52. Sem limite maximo de reservas na mesa tatica.
- RN53. Mesmo jogador nao pode ser titular e reserva simultaneamente na mesma mesa tatica.
- RN54. A mesa tatica pode ser editada ate o inicio da partida.
- RN55. A ausencia de mesa tatica nao impede o inicio da partida, o andamento da competicao nem o registro de eventos estatisticos.
- RN56. Um time pode gerar mesa tatica mesmo que o outro nao gere, pois essa visualizacao nao altera as regras da partida.

### Organizacao e comunicacao
- RN57. Apenas usuarios autenticados podem criar torneios.
- RN58. Todo torneio deve possuir formato definido.
- RN59. Formatos validos: mata-mata, grupos + mata-mata, pontos corridos, final unica.
- RN60. Todo torneio possui organizador responsavel.
- RN61. Torneio so pode iniciar com participantes suficientes.
- RN62. A preparacao da competicao deve gerar estrutura, rodadas e partidas de acordo com o formato definido.
- RN63. O torneio deve definir a quantidade de jogadores por equipe.
- RN64. As partidas devem respeitar a quantidade de jogadores definida.
- RN65. O uso de desafios e amistosos e opcional e nao impede o funcionamento dos torneios oficiais.
- RN66. Apenas usuario autenticado responsavel por um time pode propor desafio amistoso.
- RN67. Um time nao pode desafiar ele mesmo.
- RN68. O responsavel pelo time desafiado pode aceitar ou recusar o convite.
- RN69. Responsaveis pelos times envolvidos podem reagendar data e local do amistoso antes do encerramento.
- RN70. Responsaveis pelos times envolvidos podem registrar o resultado do amistoso aceito.
- RN71. Resultados de amistosos ficam no historico dos times envolvidos.
- RN72. Apenas o organizador do torneio pode publicar comunicados oficiais.
- RN73. Usuarios autenticados podem comentar em partidas pertencentes ao torneio.
- RN74. Usuarios nao autenticados nao podem comentar no feed social.
- RN75. Atualizacoes automaticas sobre jogos podem ser publicadas pelo sistema apos eventos relevantes da partida.
- RN76. Comentarios podem ser editados pelo proprio autor.
- RN77. O feed social geral pode ser visualizado por visitantes, mas apenas usuarios autenticados podem publicar, comentar, curtir ou reagir.
- RN78. Publicacoes do feed social podem conter texto, midias e hashtags.
- RN79. O feed social pode ser filtrado por hashtag para acompanhar assuntos, torneios, times ou peladas especificas.
- RN80. A estrutura e a preparacao da competicao podem ser feitas por sorteio automatico ou por montagem manual do organizador.
- RN81. A montagem manual deve utilizar apenas times aprovados no torneio.
- RN82. Um torneio finalizado pode ser repetido como nova edicao, mantendo o historico da edicao anterior.
- RN83. Ao repetir um torneio, os participantes da nova edicao devem ser definidos novamente.
- RN84. As estatisticas da edicao anterior devem permanecer arquivadas antes de reiniciar o ciclo estatistico da nova edicao.

### Partidas, andamento e estatisticas
- RN85. Partida pertence a um torneio e dois times validos.
- RN86. Apenas partidas validas geram impacto no sistema.
- RN87. Resultado atualiza classificacao, chaveamento e status da partida automaticamente.
- RN88. Nao permitir resultados invalidos.
- RN89. O resultado da partida pode ser registrado sem eventos estatisticos.
- RN90. Registrar eventos individuais no scout opcional quando desejado.
- RN91. Nota estatistica calculada automaticamente quando houver eventos.
- RN92. Nota baseada em formula com pesos.
- RN93. Considera eventos basicos na versao inicial.
- RN94. Eventos positivos e negativos afetam a nota.
- RN95. Artilharia atualizada automaticamente quando houver gols registrados.
- RN96. Na ausencia de scout detalhado, apenas o placar oficial da partida deve ser exibido.
- RN97. Eventos do scout estatistico podem ser corrigidos ou removidos pelo organizador.
- RN98. A consolidacao das estatisticas atualiza notas, artilharia, lideres de assistencias e historico dos jogadores.
- RN99. Substituicao pode ser registrada no scout estatistico independentemente da mesa tatica, desde que os jogadores envolvidos pertencam aos times da partida.
