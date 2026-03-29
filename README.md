# ⚽ Gestão de Torneios Amadores de Futebol com Estatísticas

Projeto acadêmico desenvolvido para a disciplina de **Requisitos e Projeto de Software**, com foco em **Domain-Driven Design (DDD)** e **Behavior-Driven Development (BDD)**.

O sistema tem como objetivo permitir a **gestão de torneios amadores de futebol**, incluindo organização da competição, participação de usuários e geração de estatísticas de desempenho dos jogadores.

---

# 🎯 Objetivo do Sistema

O projeto busca resolver o problema da **organização manual de competições amadoras**, oferecendo um sistema que permita:

- ⚽ Criar e gerenciar torneios de futebol  
- 🏆 Definir formatos de competição *(mata-mata, fase de grupos, pontos corridos, etc.)*  
- 👥 Permitir participação de times por meio de solicitações  
- 📅 Gerar estrutura e partidas automaticamente  
- 📊 Registrar eventos da partida *(gols, assistências e cartões)*  
- 👤 Gerar estatísticas de jogadores  
- ⭐ Calcular automaticamente notas estatísticas de desempenho  
- 📈 Acompanhar classificação, artilharia e andamento do torneio  

---

# 📌 Escopo do Projeto

O sistema permite interação entre diferentes tipos de usuários:

- **Visitante**: pode visualizar torneios disponíveis  
- **Usuário autenticado**: pode criar times, solicitar participação e gerenciar torneios  
- **Organizador**: responsável por configurar e controlar a competição  

Entre as principais funcionalidades previstas estão:

- criação e configuração de torneios  
- solicitação e aprovação de participação de times  
- gerenciamento de times, jogadores e técnicos  
- geração automática da estrutura da competição  
- registro de partidas e resultados  
- registro de eventos estatísticos  
- cálculo automático de nota estatística  
- geração de classificação e artilharia  

---

# 🧠 Abordagem de Engenharia de Software

O desenvolvimento do sistema segue práticas modernas de engenharia de software.

## 🏗 Domain-Driven Design (DDD)

Utilizado para modelar o domínio do problema de forma clara e alinhada com a realidade da aplicação.

O projeto utiliza:

- **linguagem onipresente**
- **bounded contexts**
- **modelagem estratégica do domínio**

---

## 🧪 Behavior-Driven Development (BDD)

Utilizado para especificar o comportamento esperado do sistema através de cenários.

Os cenários são descritos em arquivos `.feature` utilizando **Gherkin**, permitindo uma especificação clara das funcionalidades.

---

# 🗂 Estrutura do Projeto

```text
gestao-torneios-amadores
│
├── documentacao
│   ├── descricao-dominio.md
│   ├── linguagem-onipresente.md
│   ├── funcionalidades.md
│   ├── regras-de-negocio.md
│   ├── mapa-historias.md
│   └── cenarios-bdd.md
│
├── features
│   ├── visualizar-torneios-disponiveis.feature
│   ├── acesso-autenticado-gerenciamento-torneios.feature
│   ├── solicitar-participacao-em-torneio-aberto.feature
│   ├── avaliar-solicitacoes-de-participacao.feature
│   ├── gerenciar-times-do-usuario.feature
│   ├── gerenciar-elenco-de-jogadores.feature
│   ├── gerenciar-comissao-tecnica.feature
│   ├── vincular-time-a-usuario-responsavel.feature
│   ├── criar-e-configurar-torneio.feature
│   ├── gerenciar-participantes-aprovados-do-torneio.feature
│   ├── gerar-estrutura-da-competicao.feature
│   ├── gerar-partidas-do-torneio.feature
│   ├── registrar-resultado-da-partida.feature
│   ├── visualizar-classificacao-ou-chaveamento.feature
│   ├── registrar-eventos-estatisticos-da-partida.feature
│   └── calcular-e-visualizar-estatisticas.feature
│
├── context-mapper
│   └── torneio.cml
│
├── README.md
└── .gitignore

```

## ⚙️ Principais Conceitos do Domínio

O sistema foi modelado a partir dos principais conceitos presentes em competições de futebol:

⚙️ Principais Conceitos do Domínio

O sistema foi modelado a partir dos principais conceitos presentes em competições de futebol:

- Usuário
- Usuário organizador
- Torneio
- Time
- Jogador
- Técnico
- Partida
- Rodada
- Grupo
- Classificação
- Chaveamento
- Gol
- Assistência
- Cartão
- Estatísticas
- Nota estatística do jogador
- Artilharia

Esses conceitos são descritos formalmente no arquivo linguagem-onipresente.md.

Esses conceitos são descritos formalmente no arquivo **linguagem-onipresente.md**.

---

## ⭐ Nota Estatística do Jogador

O sistema calcula automaticamente uma nota estatística de desempenho para cada jogador em uma partida.

A nota estatística é calculada automaticamente pelo sistema por meio de uma fórmula baseada em pesos atribuídos aos eventos registrados na partida.

Eventos considerados:

- ⚽ gol
- 🎯 assistência
- 🟨 cartão amarelo
- 🟥 cartão vermelho

A partir desses eventos, o sistema calcula a nota do jogador e atualiza estatísticas da competição.

---

## 🧩 Modelagem do Domínio

A modelagem inicial do domínio foi realizada utilizando Context Mapper, permitindo a identificação dos principais Bounded Contexts do sistema.

Exemplo de contextos identificados:

- Acesso e participação
- Organização do torneio
- Competição (partidas e resultados)
- Estatísticas de jogadores

Essa modelagem representa o DDD estratégico, que orientará a fase seguinte de DDD tático e implementação do sistema.

---

## 🚀 Próximas Etapas do Projeto

Após a definição do domínio e das funcionalidades, as próximas etapas incluem:

1️⃣ Modelagem tática do domínio (DDD Tático)
2️⃣ Definição da arquitetura da aplicação
3️⃣ Implementação das entidades e serviços do domínio
4️⃣ Implementação da camada de aplicação
5️⃣ Implementação da persistência (JPA)
6️⃣ Desenvolvimento da interface web
7️⃣ Automação dos cenários BDD com Cucumber

---

## 🛠 Tecnologias Previstas

O sistema será desenvolvido utilizando:

- Java  
- Spring Boot  
- JPA / Hibernate  
- Banco de dados relacional  
- Angular ou Vaadin  
- Cucumber *(BDD)*  

---

## 👨‍💻 Autor

Projeto desenvolvido como parte da disciplina de **Requisitos e Projeto de Software**.

