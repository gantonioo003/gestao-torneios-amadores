# Descricao do Dominio

## Visao geral

O dominio do sistema e a gestao de torneios amadores de futebol com suporte ao registro opcional de dados estatisticos de partidas e jogadores, comparativos de desempenho e engajamento por meio de palpites gamificados, conversas privadas e em grupo, comunicados, desafios opcionais, feed social e moderacao da comunidade.

O sistema tem como objetivo permitir que usuarios criem, gerenciem e participem de torneios de futebol amador em diferentes formatos, como mata-mata, fase de grupos com mata-mata, pontos corridos e final unica.

Alem da gestao da competicao, o sistema permite a participacao de diferentes usuarios, possibilitando cadastro de conta como jogador ou organizador, login, edicao e exclusao da propria conta, gerenciamento completo de inscricoes de times em torneios abertos, acompanhamento de status, cancelamento de candidatura pendente, aprovacao ou rejeicao pelo organizador, chat privado com solicitacoes de conversa, desafios amistosos opcionais entre times, geracao opcional da escalacao de cada time em uma mesa tatica para cada partida, registro de palpites por usuarios autenticados ou visitantes e publicacao de postagens, comunicados e interacoes sociais no feed.

O sistema tambem permite o gerenciamento de um scout estatistico opcional das partidas, com registro, correcao e remocao de gols, assistencias, cartoes e substituicoes. Esses eventos sao independentes da existencia de mesa tatica e servem para detalhar o jogo quando o organizador quiser.

---

## Problema que o sistema resolve

Competicoes amadoras costumam ser organizadas manualmente, o que dificulta o controle de:

- torneios e campeonatos criados
- contas de usuario e autenticacao
- inscricoes de times, status das solicitacoes e lista final de participantes
- comunicacao privada entre usuarios com solicitacoes de conversa
- desafios e amistosos opcionais entre times
- jogadores e tecnicos
- visualizacao opcional da escalacao dos times em mesa tatica
- partidas e resultados
- classificacao e chaveamento
- estatisticas dos jogadores
- notas de desempenho por partida
- comparativos de desempenho entre times e jogadores
- engajamento de usuarios e visitantes em torno da competicao
- controle de mensagens privadas para evitar contato direto sem aprovacao
- feed social com postagens, fotos, videos, hashtags, curtidas, reacoes, comentarios, encaminhamento para o chat e atualizacoes automaticas sobre jogos

Alem disso, nao ha padronizacao no acompanhamento das competicoes, dificultando a organizacao e a transparencia das informacoes, e tambem nao existe espaco para que usuarios participem ativamente dando palpites, comentando partidas e acompanhando comunicados oficiais em um local centralizado.

O sistema busca centralizar essas informacoes, permitir a interacao entre organizadores e participantes, automatizar calculos importantes da competicao e oferecer um canal de engajamento por meio de palpites e feed social do torneio.

---

## Principais conceitos do dominio

Os principais conceitos do dominio sao:

- Usuario
- Conta de usuario
- Login
- Tipo de conta
- Conta de jogador
- Conta de organizador
- Usuario organizador
- Torneio
- Formato de torneio
- Edicao do torneio
- Historico de edicao
- Repeticao de torneio
- Inscricao de participante
- Candidatura de participacao
- Status da candidatura
- Chat privado
- Solicitacao de conversa
- Aba de solicitados
- Mensagem privada
- Conversa aprovada
- Desafio amistoso
- Historico de amistosos
- Time
- Tecnico
- Jogador
- Partida
- Rodada
- Grupo
- Classificacao
- Chaveamento
- Sorteio da competicao
- Montagem manual da competicao
- Mesa tatica opcional
- Esquema tatico
- Posicao
- Titular
- Reserva
- Gol
- Assistencia
- Cartao amarelo
- Cartao vermelho
- Substituicao
- Artilharia
- Nota estatistica do jogador
- Comparativo de desempenho
- Pontuacao comparativa
- palpite
- Votante
- Tipo de palpite
- Janela de votacao
- Apuracao do palpite
- Feed social do torneio
- Feed social geral
- Postagem social
- Hashtag
- Curtida
- Reacao
- Comunicado oficial
- Comentario
- Atualizacao automatica
- Notificacao
- Categoria de notificacao
- Preferencias de notificacao

---

## Funcionamento geral

Usuarios podem criar conta conforme sua funcao no futebol ou escolher uma conta comum apenas para acompanhar torneios, fazer palpites e usar feed e chat. A conta comum nao cria torneios, nao gerencia times e nao possui perfil profissional esportivo. Jogadores e membros de comissao possuem perfil profissional; treinadores criam e administram times e elencos; organizadores criam e conduzem exclusivamente torneios. Palpites sobre eventos da competicao, como vencedor de partida, campeao do torneio, artilheiro e lider de assistencias, podem ser registrados por usuarios autenticados ou visitantes identificados, sempre salvando o voto para contagem, percentuais e apuracao. Cada palpite respeita uma janela de votacao e e apurado automaticamente quando o evento alvo e concluido.

O engajamento tambem ocorre por meio do feed social. Visitantes podem visualizar o feed geral, enquanto usuarios autenticados podem publicar postagens com texto, foto, video e hashtags, comentar, curtir, reagir e encaminhar publicacoes para conversas e grupos ja liberados. O organizador pode publicar comunicados oficiais no feed do torneio e o sistema pode gerar atualizacoes automaticas sobre jogos, como resultados registrados.

Um torneio pode ser criado ja com participantes definidos ou com vagas abertas para candidaturas de entrada de times.

Durante a criacao do torneio, o organizador define tanto o formato da competicao quanto o formato de equipe, indicando quantos jogadores cada time deve ter em campo, por exemplo 3x3, 5x5, 7x7 ou 11x11.
Essas duas definicoes, assim como o organizador responsavel e o numero da edicao, tornam-se imutaveis depois da criacao. Antes do inicio, o organizador pode editar no proprio torneio apenas os dados internos permitidos, como nome, abertura de inscricoes, participantes e modo de preparacao.

Usuarios com times cadastrados podem gerenciar inscricoes em torneios abertos, solicitando participacao, acompanhando o status e cancelando candidaturas ainda pendentes. No mesmo fluxo, cabe ao organizador aprovar ou rejeitar essas solicitacoes e ajustar a lista final de participantes antes do inicio da competicao.

Usuarios autenticados tambem podem usar chat privado e criar grupos. Uma conversa privada nasce como solicitacao. Nos grupos, contatos previamente aprovados entram imediatamente; contas sem contato recebem convite; e o treinador pode adicionar diretamente profissionais vinculados ao seu elenco. O feed funciona como timeline social: a mesma conta pode publicar com sua identidade pessoal ou representar times e torneios que administra, anexar fotos ou videos, hashtags e comentarios, receber uma curtida por conta e aparecer em assuntos do momento. Publicacoes podem ser encaminhadas como cards para conversas aprovadas e grupos acessiveis. As postagens pessoais ficam em uma aba discreta do perfil usando o mesmo nome e foto da conta. Publicacoes, comentarios e perfis podem ser denunciados, gerando um registro persistente para analise de moderacao.

Os acontecimentos relevantes da plataforma podem gerar notificacoes persistidas para o usuario. O sino apresenta um resumo das mais recentes e a central de notificacoes permite consultar o historico completo, filtrar por categoria, marcar avisos como lidos, arquivar itens antigos e salvar no banco as categorias que o usuario deseja receber. Uma categoria desativada deixa de gerar novos avisos, sem apagar o historico anterior.

Fora do fluxo formal dos torneios, contas de treinador com acesso a um time podem, opcionalmente, propor desafios pela busca ou pelo perfil de outro time. O proprio perfil administrado organiza propostas recebidas, enviadas, confirmadas e encerradas; o treinador desafiado pode aceitar ou recusar, os envolvidos podem reagendar e registrar o placar, e o sistema notifica os responsaveis quando a proposta chega ou o amistoso e confirmado. Contas comuns apenas visualizam os perfis e nao recebem controles de confronto.

O organizador define o formato do torneio, gerencia os participantes aprovados e prepara a competicao, gerando estrutura, rodadas e partidas conforme o formato escolhido. Depois de adicionar os times, a competicao pode ser montada por sorteio automatico ou manualmente, permitindo que o organizador escolha a ordem dos confrontos ou a distribuicao dos times.

Antes de cada partida, o usuario responsavel pelo time ou o tecnico associado pode gerar a visualizacao da escalacao do time em uma mesa tatica, escolhendo o esquema tatico, indicando os jogadores titulares por posicao e os jogadores reservas e posicionando visualmente o time em campo. A mesa tatica e sempre opcional, nao interfere no andamento do torneio e pode ser editada ate o inicio da partida, ficando congelada apos esse momento.

Durante a competicao, o organizador registra obrigatoriamente os resultados das partidas para atualizar o andamento do torneio, incluindo classificacao, chaveamento e status das partidas. De forma independente e opcional, o organizador pode gerenciar o scout estatistico da partida, registrando, corrigindo ou removendo eventos individuais como gols, assistencias, cartoes e substituicoes. Esse scout funciona mesmo quando nao existe mesa tatica na partida.

Quando houver eventos registrados, o sistema consolida automaticamente estatisticas e rankings do torneio, atualizando notas dos jogadores, artilharia, lideres de assistencias e historico de desempenho. A partir desses dados, usuarios tambem podem gerar comparativos temporarios entre jogadores ou times, usando estatisticas, historico de partidas e posicao relativa nos rankings. O comparativo so e salvo quando o usuario escolher guardar aquela analise para consulta posterior, podendo tambem atualizar ou excluir comparativos salvos.

Ao finalizar um torneio, o organizador pode repetir a competicao como uma nova edicao. Nesse fluxo, a edicao anterior continua arquivada com participantes e estatisticas historicas, enquanto a nova edicao volta para configuracao sem participantes definidos, permitindo nova inscricao de times e nova preparacao da competicao.

