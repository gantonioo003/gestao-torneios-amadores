# Descricao do Dominio

## Visao geral

O dominio do sistema e a gestao de torneios amadores de futebol com suporte ao registro opcional de dados estatisticos de partidas e jogadores.

O sistema tem como objetivo permitir que usuarios criem, gerenciem e participem de torneios de futebol amador em diferentes formatos, como mata-mata, fase de grupos com mata-mata, pontos corridos e final unica.

Além da gestao da competicao, o sistema permite a participacao de diferentes usuarios, possibilitando a visualizacao publica de torneios, solicitacao de entrada de times e gerenciamento completo por parte do organizador.

O sistema tambem permite o registro de eventos das partidas, como gols, assistencias e cartoes, para que sejam calculadas estatisticas e notas dos jogadores quando essas informacoes forem registradas.

---

## Problema que o sistema resolve

Competicoes amadoras costumam ser organizadas manualmente, o que dificulta o controle de:

- torneios e campeonatos criados
- participacao de times e controle de inscricoes
- jogadores e tecnicos
- partidas e resultados
- classificacao e chaveamento
- estatisticas dos jogadores
- notas de desempenho por partida

Além disso, nao ha padronizacao no acompanhamento das competicoes, dificultando a organizacao e a transparencia das informacoes.

O sistema busca centralizar essas informacoes, permitir a interacao entre organizadores e participantes e automatizar calculos importantes da competicao.

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
- Gol
- Assistencia
- Cartao amarelo
- Cartao vermelho
- Artilharia
- Nota estatistica do jogador

---

## Funcionamento geral

O sistema permite que usuarios visualizem torneios disponiveis mesmo sem autenticacao.

Usuarios autenticados podem criar torneios e cadastrar seus proprios times. Um torneio pode ser criado ja com participantes definidos ou com vagas abertas para solicitacao de entrada de times.

Durante a criacao do torneio, o organizador define tanto o formato da competicao quanto o formato de equipe, indicando quantos jogadores cada time deve ter em campo, por exemplo 3x3, 5x5, 7x7 ou 11x11.

Usuarios com times cadastrados podem solicitar participacao em torneios abertos, cabendo ao organizador aprovar ou rejeitar essas solicitacoes.

O organizador define o formato do torneio, gerencia os participantes aprovados, gera a estrutura da competicao e gera as partidas.

Durante a competicao, o organizador registra obrigatoriamente os resultados das partidas para atualizar o andamento do torneio. De forma complementar, o organizador pode registrar eventos ocorridos nas partidas, como gols, assistencias e cartoes.

Quando houver eventos registrados, o sistema calcula automaticamente a nota estatistica dos jogadores por meio de uma formula baseada em pesos associados aos eventos da partida, alem de atualizar a artilharia e as estatisticas detalhadas do torneio.
