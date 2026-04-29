# Cenarios BDD

## Funcionalidades com cenarios definidos

### F1. Registrar palpites de usuarios autenticados
Dominio: dominio-engajamento
Arquivo: dominio-engajamento/src/test/resources/com/torneios/dominio/engajamento/F1-registrar-palpite.feature

Cenarios principais:
- registrar palpite sobre vencedor de partida com sucesso
- registrar palpite sobre campeao do torneio com sucesso
- registrar palpite sobre artilheiro do torneio com sucesso
- registrar palpite sobre lider de assistencias do torneio com sucesso
- impedir palpite de usuario nao autenticado
- substituir palpite anterior do mesmo usuario para o mesmo evento alvo
- alterar palpite enquanto a janela de votacao estiver aberta
- impedir alteracao de palpite apos o fechamento da janela de votacao
- impedir palpite com opcao invalida para o evento alvo
- exibir percentual atualizado por opcao enquanto a janela estiver aberta
- apurar palpite como acertado quando a opcao escolhida coincide com o resultado real
- apurar palpite como nao acertado quando a opcao escolhida diverge do resultado real
- impedir alteracao de um palpite apos a apuracao

---

### F2. Gerenciar conta de usuario e autenticacao
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

### F4. Gerenciar inscricoes e participantes do torneio
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

### F5. Gerenciar times do usuario
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F5-gerenciar-times-do-usuario.feature

Cenarios principais:
- criar um novo time com sucesso
- editar informacoes de um time do usuario
- excluir um time sem vinculo em torneio
- impedir exclusao de time vinculado a torneio

---

### F6. Gerenciar elenco de jogadores de um time
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F6-gerenciar-elenco-de-jogadores.feature

Cenarios principais:
- adicionar jogador ao elenco do time
- editar dados de um jogador do elenco
- remover jogador do elenco do time
- impedir gerenciamento do elenco por usuario nao responsavel

---

### F7. Gerenciar comissao tecnica de um time
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/F7-gerenciar-comissao-tecnica.feature

Cenarios principais:
- associar tecnico a um time
- editar dados de um tecnico do time
- remover tecnico da comissao tecnica do time
- impedir gerenciamento da comissao tecnica por usuario nao responsavel

---

### F8. Definir escalacao do time para uma partida
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/F8-escalar-time-para-partida.feature

Cenarios principais:
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

### F9. Criar e configurar torneio
Dominio: dominio-torneio
Arquivo: dominio-torneio/src/test/resources/com/torneios/dominio/torneio/F9-criar-e-configurar-torneio.feature

Cenarios principais:
- criar torneio com formato de competicao e formato de equipe validos
- impedir criacao de torneio sem formato de competicao
- impedir criacao de torneio sem definicao da quantidade de jogadores por equipe
- definir se o torneio sera aberto para solicitacao ou com participantes definidos

---

### F10. Gerenciar desafios e amistosos entre times
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

### F13. Registrar resultado da partida
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

### F15. Gerenciar sumula estatistica da partida
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/F15-gerenciar-sumula-estatistica-da-partida.feature

Cenarios principais:
- registrar gol e assistencia em uma partida
- registrar cartoes em uma partida
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

### Acesso e participacao
- RN01. Apenas usuarios autenticados podem registrar palpites.
- RN02. Apenas usuarios autenticados podem criar torneios.
- RN03. Apenas usuarios autenticados podem gerenciar candidaturas de participacao em torneios.
- RN04. Usuario deve possuir time cadastrado para enviar candidatura.
- RN05. Torneio pode ser aberto ou fechado para participacao.
- RN06. Apenas organizador pode aprovar, rejeitar e ajustar a lista final de participantes antes do inicio do torneio.
- RN54. Usuario pode cadastrar conta informando nome, email e senha.
- RN55. Email de conta de usuario deve ser unico.
- RN56. Login exige email e senha validos.
- RN57. Usuario pode editar os dados da propria conta.
- RN58. Usuario pode excluir a propria conta.
- RN65. Usuario pode acompanhar o status das candidaturas enviadas por ele.
- RN66. Usuario pode cancelar uma candidatura enquanto ela estiver pendente.
- RN67. Candidaturas ja avaliadas pelo organizador nao podem ser canceladas pelo solicitante.

### Palpites e engajamento
- RN30. Tipos de palpite suportados: vencedor de partida, campeao, artilheiro e lider de assistencias.
- RN31. Cada usuario autenticado faz no maximo um palpite por evento alvo.
- RN32. Palpite pode ser alterado enquanto a janela estiver aberta.
- RN33. Janela do palpite de vencedor de partida fecha no inicio da partida.
- RN34. Janela dos demais palpites fecha no inicio do torneio.
- RN35. Sistema exibe percentual de votos por opcao em tempo real.
- RN36. Apuracao automatica de acerto apos a conclusao do evento alvo.
- RN37. Palpites apurados sao imutaveis.

### Feed social do torneio
- RN48. Apenas o organizador do torneio pode publicar comunicados oficiais.
- RN49. Usuarios autenticados podem comentar em partidas pertencentes ao torneio.
- RN50. Usuarios nao autenticados nao podem comentar no feed social.
- RN51. Atualizacoes automaticas sobre jogos podem ser publicadas pelo sistema apos eventos relevantes da partida.
- RN52. Comentarios podem ser editados pelo proprio autor.

### Desafios e amistosos
- RN59. Apenas usuario autenticado responsavel por um time pode propor desafio amistoso.
- RN60. Um time nao pode desafiar ele mesmo.
- RN61. O responsavel pelo time desafiado pode aceitar ou recusar o convite.
- RN62. Responsaveis pelos times envolvidos podem reagendar data e local do amistoso antes do encerramento.
- RN63. Responsaveis pelos times envolvidos podem registrar o resultado do amistoso aceito.
- RN64. Resultados de amistosos ficam no historico dos times envolvidos.

### Organizacao do torneio
- RN07. Todo torneio deve possuir formato definido.
- RN08. Formatos validos: mata-mata, grupos + mata-mata, pontos corridos, final unica.
- RN09. Todo torneio possui organizador responsavel.
- RN10. Torneio so pode iniciar com participantes suficientes.
- RN11. A preparacao da competicao deve gerar estrutura, rodadas e partidas de acordo com o formato definido.
- RN12. O torneio deve definir a quantidade de jogadores por equipe.
- RN13. As partidas devem respeitar a quantidade de jogadores definida.

### Times, jogadores e tecnicos
- RN14. Time so participa se aprovado.
- RN15. Jogador pertence a um time.
- RN16. Tecnico associado ao time participante.
- RN17. Apenas jogadores validos podem ter eventos registrados.

### Escalacao da partida
- RN38. Cada time deve ter escalacao definida antes do inicio da partida.
- RN39. Escalacao definida pelo responsavel do time ou pelo tecnico.
- RN40. Esquema tatico compativel com o formato de equipe.
- RN41. Quantidade de titulares igual ao formato de equipe.
- RN42. Cada titular associado a uma posicao do esquema.
- RN43. Titulares e reservas devem pertencer ao elenco do time.
- RN44. Sem limite maximo de reservas.
- RN45. Mesmo jogador nao pode ser titular e reserva simultaneamente.
- RN46. Escalacao editavel ate o inicio da partida.

### Partidas e competicao
- RN18. Partida pertence a um torneio e dois times validos.
- RN19. Apenas partidas validas geram impacto no sistema.
- RN20. Resultado atualiza classificacao, chaveamento e status da partida automaticamente.
- RN21. Nao permitir resultados invalidos.
- RN22. O resultado da partida pode ser registrado sem eventos estatisticos.

### Estatisticas
- RN23. Registrar gols, assistencias e cartoes quando desejado.
- RN24. Nota estatistica calculada automaticamente quando houver eventos.
- RN25. Nota baseada em formula com pesos.
- RN26. Considera eventos basicos na versao inicial.
- RN27. Eventos positivos e negativos afetam a nota.
- RN28. Artilharia atualizada automaticamente quando houver gols registrados.
- RN29. Na ausencia de eventos, apenas o placar oficial da partida deve ser exibido.
- RN47. Eventos da sumula estatistica podem ser corrigidos ou removidos pelo organizador.
- RN53. A consolidacao das estatisticas atualiza notas, artilharia, lideres de assistencias e historico dos jogadores.
