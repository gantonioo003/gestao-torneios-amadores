# Cenarios BDD

## Funcionalidades com cenarios definidos

### F1. Gerenciar palpites publicos de usuarios e visitantes
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

### F2. Gerenciar ciclo de vida da conta de usuario e autenticacao
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F2-gerenciar-conta-de-usuario-e-autenticacao.feature

Cenarios principais:
- cadastrar nova conta de usuario
- realizar login com email e senha validos
- impedir login com senha incorreta
- editar dados da conta
- excluir conta de usuario
- impedir cadastro com email ja utilizado

---

### F3. Gerenciar candidatura de time em torneio aberto
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F3-gerenciar-candidatura-de-time-em-torneio-aberto.feature

Cenarios principais:
- enviar candidatura com time cadastrado
- acompanhar status das candidaturas do time
- cancelar candidatura pendente
- impedir cancelamento de candidatura ja avaliada
- impedir candidatura sem time cadastrado
- impedir candidatura de usuario nao autenticado
- impedir candidatura em torneio fechado
- impedir candidatura duplicada no mesmo torneio

---

### F4. Gerenciar inscricoes e lista final de participantes do torneio
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F4-gerenciar-inscricoes-e-participantes-do-torneio.feature

Cenarios principais:
- aprovar solicitacao e incluir time na lista final
- rejeitar solicitacao de participacao
- remover time aprovado antes do inicio do torneio
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

### F6. Gerenciar elenco e comissao tecnica do time
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F6-gerenciar-elenco-e-comissao-tecnica-do-time.feature

Cenarios principais:
- adicionar jogador ao elenco do time
- editar dados de um jogador do elenco
- remover jogador do elenco do time
- associar tecnico a um time
- editar dados de um tecnico do time
- remover tecnico da comissao tecnica do time
- impedir gerenciamento por usuario nao responsavel
- impedir remocao de jogador inexistente
- impedir remocao de tecnico inexistente

---

### F7. Gerenciar comparativos de desempenho entre times e jogadores
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

### F8. Gerenciar escalacao opcional do time para uma partida
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F8-escalar-time-para-partida.feature

Cenarios principais:
- permitir partida sem escalacao quando ela nao for obrigatoria
- impedir inicio com escalacao informada por apenas um time
- impedir inicio de partida que exige escalacao sem todos os times escalados
- definir escalacao com esquema tatico, titulares por posicao e reservas com sucesso
- permitir escalacao tanto pelo responsavel do time quanto pelo tecnico associado
- impedir escalacao por usuario que nao e responsavel nem tecnico do time
- impedir escalacao com quantidade de titulares diferente do formato de equipe do torneio
- impedir escalacao com esquema tatico incompativel com o formato de equipe
- impedir escalacao com jogador que nao pertence ao elenco do time
- impedir o mesmo jogador como titular e reserva da mesma escalacao
- editar escalacao enquanto a partida nao foi iniciada
- impedir edicao de escalacao apos o inicio da partida
- aceitar quantidade qualquer de reservas, inclusive zero

---

### F9. Gerenciar criacao e configuracao do torneio
Dominio: dominio-torneio
Arquivo: dominio-torneio/src/test/resources/com/torneios/dominio/torneio/F9-criar-e-configurar-torneio.feature

Cenarios principais:
- criar torneio com formato de competicao e formato de equipe validos
- impedir criacao de torneio sem formato de competicao
- impedir criacao de torneio sem definicao da quantidade de jogadores por equipe
- definir se o torneio sera aberto para solicitacao ou com participantes definidos

---

### F10. Gerenciar desafios e amistosos opcionais entre times
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

### F11. Preparar competicao do torneio
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F11-preparar-competicao-do-torneio.feature

Cenarios principais:
- preparar competicao por pontos corridos
- preparar competicao mata-mata
- preparar competicao com fase de grupos
- impedir preparacao sem estrutura previa da competicao

---

### F12. Gerenciar comunicados e feed social do torneio
Dominio: dominio-engajamento
Arquivo: dominio-engajamento/src/test/resources/com/torneios/dominio/engajamento/F12-gerenciar-comunicados-e-feed-social-do-torneio.feature

Cenarios principais:
- publicar comunicado oficial no feed do torneio
- impedir comunicado oficial por usuario que nao e organizador
- comentar sobre uma partida do torneio
- impedir comentario de usuario nao autenticado
- publicar atualizacao automatica sobre jogo
- editar comentario pelo proprio autor
- listar publicacoes do feed do torneio

---

### F13. Registrar placar oficial da partida
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F13-registrar-resultado-da-partida.feature

Cenarios principais:
- registrar resultado valido de uma partida
- registrar apenas o placar da partida sem informar eventos estatisticos
- atualizar automaticamente classificacao ou chaveamento apos resultado
- impedir registro por usuario nao organizador
- impedir registro para partida inexistente ou invalida

---

### F14. Gerenciar andamento da competicao
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F14-gerenciar-andamento-da-competicao.feature

Cenarios principais:
- atualizar classificacao e status da partida apos resultado
- gerenciar chaveamento em torneio mata-mata
- consultar classificacao em torneio de pontos corridos
- impedir gerenciamento de andamento sem estrutura gerada

---

### F15. Gerenciar sumula estatistica opcional da partida
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/F15-gerenciar-sumula-estatistica-da-partida.feature

Cenarios principais:
- registrar gol e assistencia em uma partida
- registrar cartoes em uma partida
- registrar substituicao quando a partida possui escalacao
- impedir substituicao quando a partida nao possui escalacao
- corrigir evento estatistico da sumula
- remover evento estatistico da sumula
- impedir gerenciamento de sumula por usuario nao organizador
- impedir registro de eventos para jogador nao pertencente ao time

---

### F16. Consolidar estatisticas e rankings do torneio
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/F16-consolidar-estatisticas-e-rankings-do-torneio.feature

Cenarios principais:
- consolidar notas, artilharia, assistencias e historico dos jogadores
- gerar ranking de artilharia
- atualizar estatisticas apos novos eventos
- nao consolidar estatisticas detalhadas quando nao houver eventos registrados

---

## Regras de negocio cobertas

### Conta e engajamento inicial
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
- RN11. Usuario pode cadastrar conta informando nome, email e senha.
- RN12. Email de conta de usuario deve ser unico.
- RN13. Login exige email e senha validos.
- RN14. Usuario pode editar os dados da propria conta.
- RN15. Usuario pode excluir a propria conta.

### Participacao no torneio
- RN16. Apenas usuarios autenticados podem gerenciar candidaturas de participacao em torneios.
- RN17. Usuario deve possuir time cadastrado para enviar candidatura.
- RN18. Torneio pode ser aberto ou fechado para participacao.
- RN19. Usuario pode acompanhar o status das candidaturas enviadas por ele.
- RN20. Usuario pode cancelar uma candidatura enquanto ela estiver pendente.
- RN21. Candidaturas ja avaliadas pelo organizador nao podem ser canceladas pelo solicitante.
- RN22. Apenas organizador pode aprovar, rejeitar e ajustar a lista final de participantes antes do inicio do torneio.
- RN23. Time so participa se estiver inscrito ou aprovado.

### Times, elenco e desempenho
- RN24. Apenas usuario autenticado responsavel pelo time pode cadastrar, editar, excluir ou consultar informacoes protegidas desse time.
- RN25. Time vinculado a torneio nao pode ser excluido quando o vinculo impedir remocao segura.
- RN26. Jogador pertence a um time.
- RN27. Tecnico associado ao time participante.
- RN28. Apenas jogadores validos podem ter eventos registrados.
- RN29. Comparativos podem ser gerados temporariamente por estatisticas, historico de partidas e rankings.
- RN30. Comparativos temporarios nao sao salvos automaticamente.
- RN31. Usuario pode salvar um comparativo escolhido.
- RN32. Usuario pode consultar comparativos salvos.
- RN33. Usuario pode atualizar comparativo salvo quando os dados mudarem.
- RN34. Usuario pode excluir comparativo salvo.
- RN35. O sistema deve impedir comparativo sem dados estatisticos suficientes.
- RN36. Escalacao e opcional quando o torneio ou a partida nao exigirem esse detalhamento.
- RN37. Escalacao definida pelo responsavel do time ou pelo tecnico.
- RN38. Esquema tatico compativel com o formato de equipe.
- RN39. Quantidade de titulares igual ao formato de equipe.
- RN40. Cada titular associado a uma posicao do esquema.
- RN41. Titulares e reservas devem pertencer ao elenco do time.
- RN42. Sem limite maximo de reservas.
- RN43. Mesmo jogador nao pode ser titular e reserva simultaneamente.
- RN44. Escalacao editavel ate o inicio da partida.
- RN45. Se a partida ou o torneio exigir escalacao, os dois times devem informar escalacao antes do inicio.
- RN46. Se um time informar escalacao em uma partida opcional, o outro time tambem deve informar para manter equilibrio de dados.

### Organizacao e comunicacao
- RN47. Apenas usuarios autenticados podem criar torneios.
- RN48. Todo torneio deve possuir formato definido.
- RN49. Formatos validos: mata-mata, grupos + mata-mata, pontos corridos, final unica.
- RN50. Todo torneio possui organizador responsavel.
- RN51. Torneio so pode iniciar com participantes suficientes.
- RN52. A preparacao da competicao deve gerar estrutura, rodadas e partidas de acordo com o formato definido.
- RN53. O torneio deve definir a quantidade de jogadores por equipe.
- RN54. As partidas devem respeitar a quantidade de jogadores definida.
- RN55. O uso de desafios e amistosos e opcional e nao impede o funcionamento dos torneios oficiais.
- RN56. Apenas usuario autenticado responsavel por um time pode propor desafio amistoso.
- RN57. Um time nao pode desafiar ele mesmo.
- RN58. O responsavel pelo time desafiado pode aceitar ou recusar o convite.
- RN59. Responsaveis pelos times envolvidos podem reagendar data e local do amistoso antes do encerramento.
- RN60. Responsaveis pelos times envolvidos podem registrar o resultado do amistoso aceito.
- RN61. Resultados de amistosos ficam no historico dos times envolvidos.
- RN62. Apenas o organizador do torneio pode publicar comunicados oficiais.
- RN63. Usuarios autenticados podem comentar em partidas pertencentes ao torneio.
- RN64. Usuarios nao autenticados nao podem comentar no feed social.
- RN65. Atualizacoes automaticas sobre jogos podem ser publicadas pelo sistema apos eventos relevantes da partida.
- RN66. Comentarios podem ser editados pelo proprio autor.

### Partidas, andamento e estatisticas
- RN67. Partida pertence a um torneio e dois times validos.
- RN68. Apenas partidas validas geram impacto no sistema.
- RN69. Resultado atualiza classificacao, chaveamento e status da partida automaticamente.
- RN70. Nao permitir resultados invalidos.
- RN71. O resultado da partida pode ser registrado sem eventos estatisticos.
- RN72. Registrar gols, assistencias, cartoes e substituicoes quando desejado.
- RN73. Nota estatistica calculada automaticamente quando houver eventos.
- RN74. Nota baseada em formula com pesos.
- RN75. Considera eventos basicos na versao inicial.
- RN76. Eventos positivos e negativos afetam a nota.
- RN77. Artilharia atualizada automaticamente quando houver gols registrados.
- RN78. Na ausencia de eventos, apenas o placar oficial da partida deve ser exibido.
- RN79. Eventos da sumula estatistica podem ser corrigidos ou removidos pelo organizador.
- RN80. A consolidacao das estatisticas atualiza notas, artilharia, lideres de assistencias e historico dos jogadores.
- RN81. Substituicao so pode ser registrada quando a partida possui escalacao.
