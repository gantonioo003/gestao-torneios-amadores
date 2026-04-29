# Descricao do Dominio

## Visao geral

O dominio do sistema e a gestao de torneios amadores de futebol com suporte ao registro opcional de dados estatisticos de partidas e jogadores e ao engajamento dos usuarios por meio de palpites, comunicados e feed social dos torneios.

O sistema tem como objetivo permitir que usuarios criem, gerenciem e participem de torneios de futebol amador em diferentes formatos, como mata-mata, fase de grupos com mata-mata, pontos corridos e final unica.

Alem da gestao da competicao, o sistema permite a participacao de diferentes usuarios, possibilitando a solicitacao de entrada de times, o gerenciamento completo por parte do organizador, a definicao da escalacao de cada time para cada partida, o registro de palpites sobre o andamento da competicao e a publicacao de comunicados e interacoes sociais no feed do torneio.

O sistema tambem permite o gerenciamento da sumula estatistica das partidas, com registro, correcao e remocao de gols, assistencias e cartoes, para que sejam consolidadas estatisticas e notas dos jogadores quando essas informacoes forem registradas.

---

## Problema que o sistema resolve

Competicoes amadoras costumam ser organizadas manualmente, o que dificulta o controle de:

- torneios e campeonatos criados
- participacao de times e controle de inscricoes
- jogadores e tecnicos
- escalacao dos times em cada partida
- partidas e resultados
- classificacao e chaveamento
- estatisticas dos jogadores
- notas de desempenho por partida
- engajamento dos usuarios em torno da competicao
- comunicados oficiais, comentarios e atualizacoes automaticas sobre jogos

Alem disso, nao ha padronizacao no acompanhamento das competicoes, dificultando a organizacao e a transparencia das informacoes, e tambem nao existe espaco para que usuarios participem ativamente dando palpites, comentando partidas e acompanhando comunicados oficiais em um local centralizado.

O sistema busca centralizar essas informacoes, permitir a interacao entre organizadores e participantes, automatizar calculos importantes da competicao e oferecer um canal de engajamento por meio de palpites e feed social do torneio.

---

## Principais conceitos do dominio

Os principais conceitos do dominio sao:

- Usuario
- Usuario organizador
- Torneio
- Formato de torneio
- Solicitacao de participacao
- Time
- Tecnico
- Jogador
- Partida
- Rodada
- Grupo
- Classificacao
- Chaveamento
- Escalacao
- Esquema tatico
- Posicao
- Titular
- Reserva
- Gol
- Assistencia
- Cartao amarelo
- Cartao vermelho
- Artilharia
- Nota estatistica do jogador
- Palpite
- Tipo de palpite
- Janela de votacao
- Apuracao do palpite
- Feed social do torneio
- Comunicado oficial
- Comentario
- Atualizacao automatica

---

## Funcionamento geral

Usuarios autenticados podem criar torneios, cadastrar seus proprios times e registrar palpites sobre eventos da competicao, como vencedor de partida, campeao do torneio, artilheiro e lider de assistencias. Cada palpite respeita uma janela de votacao e e apurado automaticamente quando o evento alvo e concluido.

O engajamento tambem ocorre por meio do feed social do torneio. O organizador pode publicar comunicados oficiais, usuarios autenticados podem comentar em partidas do torneio e o sistema pode gerar atualizacoes automaticas sobre jogos, como resultados registrados.

Um torneio pode ser criado ja com participantes definidos ou com vagas abertas para solicitacao de entrada de times.

Durante a criacao do torneio, o organizador define tanto o formato da competicao quanto o formato de equipe, indicando quantos jogadores cada time deve ter em campo, por exemplo 3x3, 5x5, 7x7 ou 11x11.

Usuarios com times cadastrados podem solicitar participacao em torneios abertos, cabendo ao organizador aprovar ou rejeitar essas solicitacoes.

O organizador define o formato do torneio, gerencia os participantes aprovados e prepara a competicao, gerando estrutura, rodadas e partidas conforme o formato escolhido.

Antes de cada partida, o usuario responsavel pelo time ou o tecnico associado define a escalacao do time para aquela partida, escolhendo o esquema tatico, indicando os jogadores titulares por posicao e os jogadores reservas. A escalacao pode ser editada ate o inicio da partida e fica congelada apos esse momento.

Durante a competicao, o organizador registra obrigatoriamente os resultados das partidas para atualizar o andamento do torneio, incluindo classificacao, chaveamento e status das partidas. De forma complementar, o organizador pode gerenciar a sumula estatistica, registrando, corrigindo ou removendo eventos opcionais como gols, assistencias e cartoes.

Quando houver eventos registrados, o sistema consolida automaticamente estatisticas e rankings do torneio, atualizando notas dos jogadores, artilharia, lideres de assistencias e historico de desempenho.
