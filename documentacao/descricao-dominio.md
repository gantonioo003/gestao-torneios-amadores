# Descrição do Domínio

## Visão geral

O domínio do sistema é a gestão de torneios amadores de futebol com suporte ao registro de dados estatísticos de partidas e jogadores.

O sistema tem como objetivo permitir que usuários criem, gerenciem e participem de torneios de futebol amador em diferentes formatos, como mata-mata, fase de grupos com mata-mata, pontos corridos e final única.

Além da gestão da competição, o sistema permite a participação de diferentes usuários, possibilitando a visualização pública de torneios, solicitação de entrada de times e gerenciamento completo por parte do organizador.

O sistema também permite o registro de eventos das partidas, como gols, assistências e cartões, para que sejam calculadas automaticamente estatísticas e notas dos jogadores.

---

## Problema que o sistema resolve

Competições amadoras costumam ser organizadas manualmente, o que dificulta o controle de:

- torneios e campeonatos criados  
- participação de times e controle de inscrições  
- jogadores e técnicos  
- partidas e resultados  
- classificação e chaveamento  
- estatísticas dos jogadores  
- notas de desempenho por partida  

Além disso, não há padronização no acompanhamento das competições, dificultando a organização e a transparência das informações.

O sistema busca centralizar essas informações, permitir a interação entre organizadores e participantes e automatizar cálculos importantes da competição.

---

## Principais conceitos do domínio

Os principais conceitos do domínio são:

- Usuário  
- Usuário organizador  
- Torneio  
- Formato de torneio  
- Solicitação de participação  
- Time  
- Técnico  
- Jogador  
- Partida  
- Rodada  
- Grupo  
- Classificação  
- Chaveamento  
- Gol  
- Assistência  
- Cartão amarelo  
- Cartão vermelho  
- Artilharia  
- Nota estatística do jogador  

---

## Funcionamento geral

O sistema permite que usuários visualizem torneios disponíveis mesmo sem autenticação.

Usuários autenticados podem criar torneios e cadastrar seus próprios times. Um torneio pode ser criado já com participantes definidos ou com vagas abertas para solicitação de entrada de times.

Usuários com times cadastrados podem solicitar participação em torneios abertos, cabendo ao organizador aprovar ou rejeitar essas solicitações.

O organizador define o formato do torneio, gerencia os participantes aprovados e gera a estrutura da competição e as partidas.

Durante a competição, o organizador registra os resultados das partidas e os eventos ocorridos, como gols, assistências e cartões.

A partir desses dados, o sistema calcula automaticamente a nota estatística dos jogadores por meio de uma fórmula baseada em pesos associados aos eventos registrados na partida, além de atualizar automaticamente a classificação, a artilharia e o andamento do torneio.