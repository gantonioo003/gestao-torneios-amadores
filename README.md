# Gestao de Torneios Amadores

Projeto academico para gestao de torneios amadores de futebol, com foco em DDD, BDD, arquitetura limpa, backend Spring Boot, frontend Angular e infraestrutura com persistencia em MySQL.

A proposta evolui de um simples gerenciador de torneios para uma plataforma social de futebol amador: times, jogadores, torneios, partidas, palpites, feed, chat privado, amistosos e estatisticas convivem no mesmo ecossistema.

## Objetivo

O sistema apoia organizadores, responsaveis por times, jogadores e visitantes em um fluxo completo:

- criar conta, autenticar, editar dados e excluir a propria conta;
- gerenciar comunicacao privada entre usuarios por solicitacao de conversa;
- cadastrar times e profissionais esportivos;
- organizar torneios, participantes, inscricoes e novas edicoes;
- preparar competicoes com estrutura, rodadas e partidas;
- visualizar escalacao opcional em mesa tatica;
- registrar placar oficial e atualizar andamento da competicao;
- registrar scout estatistico opcional independente da mesa tatica;
- consolidar rankings, artilharia, assistencias, notas e historico;
- publicar e consultar feed social, comunicados, comentarios e reacoes;
- registrar palpites publicos e desafios amistosos opcionais.

## Funcionalidades

As funcionalidades completas estao descritas em [`documentacao/funcionalidades.md`](documentacao/funcionalidades.md).

Resumo atual:

- F1: palpites publicos;
- F2: conta de usuario e autenticacao;
- F3: comunicacao privada entre usuarios;
- F4: inscricoes e participantes do torneio;
- F5: times do usuario;
- F6: profissionais esportivos;
- F7: comparativos de desempenho;
- F8: mesa tatica opcional;
- F9: ciclo estrutural do torneio;
- F10: desafios e amistosos;
- F11: feed social;
- F12: placar oficial e andamento da competicao;
- F13: scout estatistico opcional;
- F14: consolidacao historica de estatisticas e rankings;
- F15: espaco reservado para evolucao futura.

## Arquitetura

O projeto segue uma organizacao modular inspirada em DDD e arquitetura limpa.

Camadas principais:

- `dominio-*`: regras centrais de negocio, entidades, value objects, servicos de dominio e repositorios abstratos;
- `aplicacao`: casos de uso e orquestracao entre dominios;
- `infraestrutura`: implementacoes tecnicas, persistencia e repositorios concretos;
- `apresentacao-backend`: API REST com Spring Boot;
- `apresentacao-frontend`: interface web em Angular;
- `documentacao`: artefatos de requisitos, regras, BDD, linguagem onipresente, prototipos e mapa de historias.

Contextos de dominio:

- `dominio-compartilhado`;
- `dominio-participacao`;
- `dominio-torneio`;
- `dominio-competicao`;
- `dominio-estatisticas`;
- `dominio-engajamento`.

## Modelagem DDD

A modelagem estrategica esta centralizada no arquivo [`torneio.cml`](torneio.cml), na raiz do repositorio.

Esse arquivo representa:

- subdominios do projeto;
- bounded contexts;
- relacionamentos entre contextos;
- agregados, entidades, value objects, servicos e repositorios;
- separacao entre participacao, torneio, competicao, estatisticas, engajamento e compartilhado.

## BDD e Testes

Os cenarios BDD foram escritos em Gherkin e automatizados com Cucumber.

As features e steps ficam distribuidos por dominio, seguindo a responsabilidade principal de cada funcionalidade:

- `dominio-participacao/src/test/resources`;
- `dominio-torneio/src/test/resources`;
- `dominio-competicao/src/test/resources`;
- `dominio-estatisticas/src/test/resources`;
- `dominio-engajamento/src/test/resources`.

Cada modulo de dominio possui estrutura propria de teste com:

- `RunCucumber.java`;
- classes auxiliares de funcionalidade;
- `steps/`;
- repositorios em memoria para apoiar os cenarios.

## Estrutura do Repositorio

```text
gestao-torneios-amadores/
|-- aplicacao/
|-- apresentacao-backend/
|-- apresentacao-frontend/
|-- documentacao/
|   |-- cenarios-bdd.md
|   |-- descricao-dominio.md
|   |-- funcionalidades.md
|   |-- linguagem-onipresente.md
|   |-- mapa-historias.md
|   |-- regras-de-negocio.md
|   `-- prototipos/
|-- dominio-compartilhado/
|-- dominio-competicao/
|-- dominio-engajamento/
|-- dominio-estatisticas/
|-- dominio-participacao/
|-- dominio-torneio/
|-- features/
|-- infraestrutura/
|-- pai/
|-- scripts/
|-- docker-compose.yml
|-- pom.xml
|-- torneio.cml
|-- mvnw
`-- mvnw.cmd
```

## Requisitos

Para executar localmente sem Docker:

- Java 21 ou superior;
- Maven Wrapper do proprio projeto;
- Node.js compativel com Angular 19;
- MySQL 8.4 ou superior.

Para executar com Docker:

- Docker;
- Docker Compose.

## Execucao Local

Verificar Maven:

```powershell
.\mvnw.cmd -v
```

Compilar backend com dependencias:

```powershell
.\mvnw.cmd -pl apresentacao-backend -am -DskipTests compile
```

Executar testes:

```powershell
.\mvnw.cmd test
```

Executar um modulo especifico:

```powershell
.\mvnw.cmd test -pl dominio-torneio -am
```

Subir frontend Angular:

```powershell
cd apresentacao-frontend\src\main\angular
npm install
npm start
```

## Execucao com Docker

Subir banco, backend e frontend:

```powershell
docker compose up --build
```

Servicos:

- Frontend: `http://localhost:4200`;
- Backend: `http://localhost:8080`;
- Swagger/OpenAPI: `http://localhost:8080/swagger-ui.html`;
- MySQL: `localhost:3306`.

Parar os servicos:

```powershell
docker compose down
```

Remover tambem o volume do banco:

```powershell
docker compose down -v
```

## Banco de Dados

Configuracao padrao local:

- database: `torneios`;
- usuario Docker: `torneios`;
- senha Docker: `torneios`;
- root password Docker: `root`.

O backend aceita variaveis de ambiente Spring, entao no Docker ele aponta para o servico `mysql`. Fora do Docker, usa `localhost` como padrao.

## Documentacao

Principais artefatos:

- [`documentacao/descricao-dominio.md`](documentacao/descricao-dominio.md);
- [`documentacao/linguagem-onipresente.md`](documentacao/linguagem-onipresente.md);
- [`documentacao/funcionalidades.md`](documentacao/funcionalidades.md);
- [`documentacao/regras-de-negocio.md`](documentacao/regras-de-negocio.md);
- [`documentacao/mapa-historias.md`](documentacao/mapa-historias.md);
- [`documentacao/cenarios-bdd.md`](documentacao/cenarios-bdd.md);
- [`documentacao/prototipos`](documentacao/prototipos).

## Estado Atual

O projeto possui modelagem de dominio, documentacao, cenarios BDD, camadas de dominio, aplicacao, infraestrutura, backend e frontend. A implementacao ainda pode evoluir em cobertura de testes integrados, refinamento de persistencia e integracao completa entre todas as telas e endpoints, mas a base estrutural ja representa o projeto completo.

## Autor

Projeto desenvolvido para a disciplina de Requisitos e Projeto de Software.
