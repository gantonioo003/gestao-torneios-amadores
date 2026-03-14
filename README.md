# ⚽ Gestão de Torneios Amadores de Futebol com Estatísticas

Projeto acadêmico desenvolvido para a disciplina de **Requisitos e Projeto de Software**, com foco em **Domain-Driven Design (DDD)** e **Behavior-Driven Development (BDD)**.

O sistema tem como objetivo permitir a **gestão de torneios amadores de futebol**, incluindo organização da competição, registro de partidas e geração de estatísticas de desempenho dos jogadores.

---

# 🎯 Objetivo do Sistema

O projeto busca resolver o problema da **organização manual de competições amadoras**, oferecendo um sistema que permita:

- ⚽ Criar e gerenciar torneios de futebol
- 🏆 Definir formatos de competição  
  *(mata-mata, fase de grupos, pontos corridos, etc.)*
- 📅 Registrar partidas e resultados
- 📊 Registrar eventos da partida *(gols, assistências e cartões)*
- 👤 Gerar estatísticas de jogadores
- ⭐ Calcular automaticamente notas estatísticas de desempenho
- 📈 Acompanhar classificação, artilharia e andamento do torneio

---

# 📌 Escopo do Projeto

O sistema permitirá que um **organizador** crie e gerencie torneios e registre informações das partidas para acompanhar o desempenho dos times e jogadores.

Entre as principais funcionalidades previstas estão:

- criação de torneios
- definição do formato da competição
- cadastro de times
- cadastro de jogadores
- registro de partidas
- registro de gols e assistências
- registro de cartões
- cálculo automático da nota estatística dos jogadores
- geração de classificação
- acompanhamento da artilharia

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
│   ├── registrar-gols-assistencias.feature
│   ├── registrar-cartoes.feature
│   └── calcular-nota-estatistica.feature
│
├── context-mapper
│   └── torneio.cml
│
├── README.md
└── .gitignore

## ⚙️ Principais Conceitos do Domínio

O sistema foi modelado a partir dos principais conceitos presentes em competições de futebol:

- Torneio  
- Time  
- Jogador  
- Técnico  
- Partida  
- Rodada  
- Gol  
- Assistência  
- Cartão  
- Estatísticas  
- Nota estatística do jogador  
- Classificação  
- Artilharia  

Esses conceitos são descritos formalmente no arquivo **linguagem-onipresente.md**.

---

## ⭐ Nota Estatística do Jogador

O sistema calcula automaticamente uma **nota estatística de desempenho** para cada jogador em uma partida.

Todos os jogadores iniciam com **nota base 5.0**, e eventos registrados durante a partida alteram essa pontuação.

Eventos considerados:

- ⚽ gol  
- 🎯 assistência  
- 🟨 cartão amarelo  
- 🟥 cartão vermelho  

A nota é calculada automaticamente pelo sistema com base nesses eventos.

---

## 🧩 Modelagem do Domínio

A modelagem inicial do domínio foi realizada utilizando **Context Mapper**, permitindo a identificação dos principais **Bounded Contexts** do sistema.

Exemplo de contextos identificados:

- Organização do Torneio  
- Competição *(partidas e resultados)*  
- Estatísticas de jogadores  

Essa modelagem representa o **DDD estratégico**, que orientará a fase seguinte de **DDD tático** e implementação do sistema.

---

## 🚀 Próximas Etapas do Projeto

Após a definição do domínio e das funcionalidades, as próximas etapas incluem:

1️⃣ Modelagem tática do domínio *(DDD Tático)*  
2️⃣ Definição da arquitetura da aplicação  
3️⃣ Implementação das entidades e serviços do domínio  
4️⃣ Implementação da camada de aplicação  
5️⃣ Implementação da persistência *(JPA)*  
6️⃣ Desenvolvimento da interface web  

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
