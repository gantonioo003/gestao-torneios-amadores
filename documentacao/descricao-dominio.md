# Descricao do Dominio

## Visao geral

O dominio do sistema e a gestao de torneios amadores de futebol com suporte ao registro opcional de dados estatisticos de partidas e jogadores e ao engajamento dos usuarios por meio de palpites sobre os eventos da competicao.

O sistema tem como objetivo permitir que usuarios criem, gerenciem e participem de torneios de futebol amador em diferentes formatos, como mata-mata, fase de grupos com mata-mata, pontos corridos e final unica.

Alem da gestao da competicao, o sistema permite a participacao de diferentes usuarios, possibilitando a solicitacao de entrada de times, o gerenciamento completo por parte do organizador, a definicao da escalacao de cada time para cada partida e o registro de palpites sobre o andamento da competicao.

O sistema tambem permite o registro de eventos das partidas, como gols, assistencias, cartoes e substituicoes, para que sejam calculadas estatisticas e notas dos jogadores quando essas informacoes forem registradas.

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

Alem disso, nao ha padronizacao no acompanhamento das competicoes, dificultando a organizacao e a transparencia das informacoes, e tambem nao existe espaco para que usuarios participem ativamente dando palpites sobre o desfecho dos jogos e do torneio.

O sistema busca centralizar essas informacoes, permitir a interacao entre organizadores e participantes, automatizar calculos importantes da competicao e oferecer um canal de engajamento por meio de palpites.

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
- Substituicao
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

---

## Funcionamento geral

Usuarios autenticados podem criar torneios, cadastrar seus proprios times e registrar palpites sobre eventos da competicao, como vencedor de partida, campeao do torneio, artilheiro e lider de assistencias. Cada palpite respeita uma janela de votacao e e apurado automaticamente quando o evento alvo e concluido.

Um torneio pode ser criado ja com participantes definidos ou com vagas abertas para solicitacao de entrada de times.

Durante a criacao do torneio, o organizador define tanto o formato da competicao quanto o formato de equipe, indicando quantos jogadores cada time deve ter em campo, por exemplo 3x3, 5x5, 7x7 ou 11x11.

Usuarios com times cadastrados podem solicitar participacao em torneios abertos, cabendo ao organizador aprovar ou rejeitar essas solicitacoes.

O organizador define o formato do torneio, gerencia os participantes aprovados, gera a estrutura da competicao e gera as partidas.

Antes de cada partida, o usuario responsavel pelo time ou o tecnico associado define a escalacao do time para aquela partida, escolhendo o esquema tatico, indicando os jogadores titulares por posicao e os jogadores reservas. A escalacao pode ser editada ate o inicio da partida e fica congelada apos esse momento.

Durante a competicao, o organizador registra obrigatoriamente os resultados das partidas para atualizar o andamento do torneio. De forma complementar, o organizador pode registrar eventos ocorridos nas partidas, como gols, assistencias, cartoes e substituicoes. Substituicoes sao registradas apos o termino da partida e devem envolver um titular e um reserva da escalacao do mesmo time.

Quando houver eventos registrados, o sistema calcula automaticamente a nota estatistica dos jogadores por meio de uma formula baseada em pesos associados aos eventos da partida, alem de atualizar a artilharia e as estatisticas detalhadas do torneio.
