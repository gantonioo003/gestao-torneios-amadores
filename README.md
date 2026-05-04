# Gestao de Torneios Amadores de Futebol com Estatisticas

Projeto academico desenvolvido para a disciplina de Requisitos e Projeto de Software, com foco em Domain-Driven Design (DDD), Behavior-Driven Development (BDD) e arquitetura modular com Maven.

O sistema tem como objetivo apoiar a gestao de torneios amadores de futebol, incluindo cadastro e organizacao de torneios, participacao de times, gerenciamento da competicao, engajamento social em formato de feed e chat, palpites publicos e consolidacao opcional de estatisticas de desempenho.

## Visao Geral

O projeto foi modelado para atender um cenario em que competicoes amadoras sao organizadas manualmente, com pouca padronizacao e alto risco de inconsistencias em inscricoes, partidas, classificacao e estatisticas.

O fluxo principal do sistema depende do registro de torneios, participantes, partidas e placares. O registro de eventos detalhados, como gols por jogador, assistencias, cartoes e substituicoes, e complementar: o torneio pode acontecer normalmente apenas com o resultado oficial de cada partida, e as estatisticas so aparecem quando esses eventos forem informados.

O sistema proposto permite:

- criar e configurar torneios
- cadastrar conta como jogador ou organizador, realizar login, editar dados e excluir conta
- definir formato de competicao e formato de equipe
- cadastrar times e gerenciar elenco, jogadores e tecnico
- permitir que jogadores usem a plataforma para buscar times e acompanhar oportunidades
- gerenciar inscricoes de times, com candidatura, acompanhamento de status, cancelamento quando pendente, avaliacao e lista final de participantes
- usar chat privado com aba de solicitados antes de liberar mensagens entre usuarios
- propor, aceitar, reagendar e registrar desafios amistosos entre times
- usar amistosos apenas como recurso opcional, sem obrigar o fluxo dos torneios
- preparar a competicao com estrutura, rodadas e partidas por sorteio automatico ou montagem manual
- repetir torneios finalizados como novas edicoes mantendo o historico anterior
- definir escalacao opcionalmente, ou exigir escalacao quando a regra da partida/torneio pedir
- publicar postagens no feed social geral com midia e hashtags
- publicar comunicados, comentarios, curtidas, reacoes e atualizacoes automaticas no feed social
- registrar palpites publicos de usuarios e visitantes, salvando os votos para contagem e apuracao
- registrar resultados oficiais de partidas
- gerenciar sumula estatistica opcional com gols, assistencias, cartoes e substituicoes condicionadas a escalacao
- consolidar notas, artilharia, lideres de assistencias, historico dos jogadores e historico estatistico por edicao quando houver eventos registrados
- gerar comparativos de desempenho entre times e jogadores, salvando e consultando apenas os comparativos escolhidos
- acompanhar classificacao, chaveamento e andamento das partidas

## Estado Atual do Projeto

O projeto encontra-se estruturado em modulos de dominio e ja possui:

- documentacao funcional e de negocio
- linguagem onipresente
- mapa de historias do usuario
- modelagem com Context Mapper
- cenarios BDD em arquivos `.feature`
- automacao Cucumber distribuida por dominio com `steps` e repositorios em memoria
- implementacao da camada de dominio nos modulos principais
- prototipos de baixa e alta fidelidade na documentacao
- Maven Wrapper configurado no repositorio

No estado atual, o foco principal esta na Entrega 1, com enfase em modelagem de dominio, especificacao comportamental e organizacao da base para a implementacao posterior das demais camadas.

## Abordagem Arquitetural

O projeto segue uma organizacao modular orientada a contexto de negocio. Cada modulo representa um conjunto coeso de responsabilidades do dominio.

### Modulos atuais

- `dominio-compartilhado`: ids, entidades compartilhadas, enumeracoes, excecoes e eventos de dominio reutilizaveis
- `dominio-participacao`: conta de usuario com tipo jogador ou organizador, autenticacao, inscricoes e participantes do torneio, times, jogadores, tecnico e responsavel do time
- `dominio-torneio`: criacao, configuracao e repeticao do torneio, participantes aprovados, organizador, edicoes e estrutura da competicao
- `dominio-competicao`: partidas, resultados, rodadas, classificacao, chaveamento, escalacao opcional e preparacao da competicao por sorteio ou montagem manual
- `dominio-estatisticas`: eventos estatisticos opcionais, nota estatistica, desempenho e artilharia
- `dominio-engajamento`: palpites publicos, chat privado com solicitacoes de conversa, desafios amistosos opcionais entre times, postagens sociais, comunicados oficiais, comentarios, curtidas, reacoes e feed social
- `pai`: modulo pai Maven com configuracao compartilhada

## Domain-Driven Design

O projeto adota DDD como base para organizacao do conhecimento de negocio e para separacao de responsabilidades entre contextos.

### Subdominios e contextos principais

- Participacao
- Torneio
- Competicao
- Estatisticas
- Engajamento
- Compartilhado

A modelagem estrategica foi registrada em [torneio.cml](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/torneio.cml).

## Behavior-Driven Development

Os comportamentos esperados do sistema foram especificados com Cucumber e Gherkin.

As features estao distribuidas por dominio em:

- `dominio-participacao/src/test/resources`
- `dominio-torneio/src/test/resources`
- `dominio-competicao/src/test/resources`
- `dominio-estatisticas/src/test/resources`
- `dominio-engajamento/src/test/resources`

Cada modulo tambem possui automacao de teste com:

- `RunCucumber.java`
- classe auxiliar de funcionalidade do dominio
- `steps/` com step definitions
- repositorios e consultas em memoria para sustentar os cenarios BDD

## Estrutura do Repositorio

```text
gestao-torneios-amadores
|
|-- documentacao/
|   |-- descricao-dominio.md
|   |-- linguagem-onipresente.md
|   |-- funcionalidades.md
|   |-- regras-de-negocio.md
|   |-- mapa-historias.md
|   |-- cenarios-bdd.md
|   `-- prototipos/
|       |-- baixa-fidelidade/
|       `-- alta-fidelidade/
|
|-- dominio-compartilhado/
|-- dominio-participacao/
|-- dominio-torneio/
|-- dominio-competicao/
|-- dominio-estatisticas/
|-- dominio-engajamento/
|
|-- .mvn/
|-- pai/
|-- pom.xml
|-- mvnw
|-- mvnw.cmd
|-- README.md
`-- torneio.cml
```

## Estrutura de Dominio Implementada

Atualmente, a camada `src/main/java` ja possui uma base inicial de implementacao em todos os modulos de dominio.

### `dominio-compartilhado`

Inclui elementos comuns reutilizados por outros modulos, como:

- `Usuario` e `UsuarioId`
- `Jogador` e `JogadorId`
- `Tecnico` e `TecnicoId`
- `TimeId`
- `TorneioId`
- `PartidaId`
- enumeracoes de formato, status e tipo de evento
- excecoes de dominio
- eventos de dominio

### `dominio-participacao`

Inclui classes voltadas a:

- autenticacao de acesso
- conta de usuario e login
- tipo de conta jogador ou organizador
- inscricoes e participantes do torneio
- gestao de times
- gestao integrada de jogadores e tecnico do time
- definicao de responsavel do time

### `dominio-torneio`

Inclui classes voltadas a:

- torneio
- edicoes e historico de edicoes do torneio
- participante do torneio
- estrutura da competicao por sorteio ou montagem manual
- regras do organizador

### `dominio-competicao`

Inclui classes voltadas a:

- partida
- resultado da partida
- classificacao
- chaveamento
- escalacao opcional da partida
- rodada
- preparacao da competicao com partidas e rodadas

### `dominio-engajamento`

Inclui classes voltadas a:

- palpites de usuarios autenticados
- apuracao automatica de palpites
- chat privado com aba de solicitados, aprovacao de conversa e mensagens
- desafios e amistosos entre times
- historico de amistosos dos times
- comunicados oficiais do torneio
- postagens sociais com hashtags e midias
- comentarios em partidas
- curtidas e reacoes no feed
- atualizacoes automaticas no feed social

### `dominio-estatisticas`

Inclui classes voltadas a:

- eventos estatisticos opcionais
- subclasses de eventos como gol e cartoes
- substituicoes quando houver escalacao
- nota estatistica quando houver eventos registrados
- consolidacao de desempenho, rankings e historico dos jogadores
- arquivamento de estatisticas por edicao do torneio
- artilharia baseada nos gols registrados
- comparativos temporarios e salvos de desempenho entre times e jogadores

## Documentacao do Projeto

Os principais artefatos da Entrega 1 estao organizados em [documentacao](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao):

- [descricao-dominio.md](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/descricao-dominio.md)
- [linguagem-onipresente.md](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/linguagem-onipresente.md)
- [funcionalidades.md](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/funcionalidades.md)
- [regras-de-negocio.md](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/regras-de-negocio.md)
- [mapa-historias.md](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/mapa-historias.md)
- [cenarios-bdd.md](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/cenarios-bdd.md)
- [prototipo-baixa-fidelidade.png](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/prototipos/baixa-fidelidade/prototipo-baixa-fidelidade.png)
- [prototipo-alta-fidelidade.png](C:/Users/ganto/OneDrive/Área%20de%20Trabalho/gestao-torneios-amadores/documentacao/prototipos/alta-fidelidade/prototipo-alta-fidelidade.png)

## Tecnologias Utilizadas

- Java
- Maven
- Maven Wrapper
- Spring Boot Parent no modulo pai
- JUnit 5
- Cucumber
- Context Mapper

## Como Executar

### Verificar a versao do Maven Wrapper

```powershell
.\mvnw.cmd -v
```

### Executar testes Maven

```powershell
.\mvnw.cmd test
```

No estado atual, a suite cobre os modulos de dominio com cenarios Cucumber e testes auxiliares, incluindo os fluxos de torneio, participacao, competicao, estatisticas e engajamento.

### Executar um modulo especifico

```powershell
.\mvnw.cmd test -pl dominio-torneio -am
```

## Proximas Etapas

As proximas evolucoes previstas para o projeto incluem:

- ampliar a implementacao dos comportamentos de dominio
- introduzir camada de aplicacao
- implementar persistencia objeto-relacional
- desenvolver camada de apresentacao web

## Autor

Projeto desenvolvido como parte da disciplina de Requisitos e Projeto de Software.
