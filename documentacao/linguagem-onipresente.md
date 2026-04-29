# Linguagem Onipresente

## Termos principais do dominio

### Usuario
Pessoa que utiliza o sistema. Para registrar palpites, criar torneios, gerenciar times e participar de qualquer fluxo do sistema, o usuario precisa estar autenticado.

### Conta de usuario
Cadastro que identifica o usuario na plataforma, contendo nome, email e senha. Pode ser criada, editada e excluida pelo proprio usuario.

### Login
Processo de autenticacao realizado com email e senha validos para liberar o acesso do usuario as funcionalidades protegidas.

### Usuario organizador
Usuario responsavel por criar e gerenciar um torneio.

### Torneio
Competicao de futebol amador criada por um organizador.

### Formato de torneio
Estrutura do torneio. Pode ser:
- mata-mata
- fase de grupos + mata-mata
- pontos corridos
- final unica

### Formato de equipe
Define a quantidade de jogadores em campo por time em uma partida, como 3x3, 5x5, 7x7 ou 11x11.

### Solicitacao de participacao
Pedido realizado por um usuario para inscrever seu time em um torneio aberto.

### Time
Equipe participante de um torneio, vinculada a um usuario responsavel.

### Tecnico
Responsavel tecnico associado a um time.

### Jogador
Atleta vinculado a um time e participante das partidas.

### Partida
Jogo realizado entre dois times dentro de um torneio. Toda partida possui um resultado oficial e pode possuir, opcionalmente, eventos estatisticos detalhados.

### Rodada
Conjunto de partidas de uma mesma etapa da competicao.

### Grupo
Divisao de times na fase de grupos do torneio.

### Classificacao
Tabela com a pontuacao e posicao dos times na competicao.

### Chaveamento
Estrutura visual e logica das fases eliminatorias do torneio.

### Escalacao
Configuracao definida pelo responsavel do time ou pelo tecnico para uma partida especifica, contendo o esquema tatico, os jogadores titulares por posicao e os jogadores reservas. A escalacao pode ser editada ate o inicio da partida e fica congelada apos esse momento.

### Esquema tatico
Distribuicao das posicoes dos jogadores titulares em campo, compativel com o formato de equipe do torneio. Exemplos: 4-4-2 ou 4-3-3 no 11x11; 2-1-1 no 5x5; 1-1-1 no 3x3.

### Posicao
Funcao tatica atribuida a cada jogador titular dentro do esquema tatico, como goleiro, defensor, meio-campista ou atacante.

### Titular
Jogador escalado para iniciar a partida em uma posicao definida pelo esquema tatico.

### Reserva
Jogador relacionado para a partida que nao inicia como titular, mas fica disponivel para compor a equipe conforme a necessidade do time.

### Sumula estatistica
Registro opcional dos eventos detalhados de uma partida, como gols, assistencias e cartoes. Pode ser corrigida ou ajustada pelo organizador.

### Gol
Evento estatistico registrado quando um jogador marca para seu time em uma partida.

### Assistencia
Evento estatistico registrado quando um jogador contribui diretamente para um gol.

### Cartao amarelo
Evento estatistico de advertencia recebido por um jogador durante a partida.

### Cartao vermelho
Evento estatistico de expulsao recebido por um jogador durante a partida.

### Artilharia
Ranking dos jogadores com maior numero de gols no torneio, calculado quando houver registro de gols.

### Nota estatistica
Pontuacao calculada automaticamente pelo sistema com base em uma formula que considera os eventos registrados para um jogador em uma partida. So existe quando houver eventos registrados.

### Consolidacao de estatisticas
Processo que agrupa os eventos registrados na sumula para atualizar notas, artilharia, lideres de assistencias e historico dos jogadores.

### Historico do jogador
Conjunto de eventos e desempenho acumulado de um jogador dentro do torneio.

### Palpite
Registro feito por um usuario autenticado expressando sua aposta sobre o desfecho de um evento do sistema. Pode ser de tipo: vencedor de partida, campeao do torneio, artilheiro do torneio ou lider de assistencias do torneio.

### Tipo de palpite
Classifica o evento alvo do palpite. Cada tipo possui sua propria janela de votacao e suas opcoes validas:
- vencedor de partida: opcoes sao os dois times da partida
- campeao do torneio: opcoes sao os times participantes
- artilheiro: opcoes sao os jogadores dos times participantes
- lider de assistencias: opcoes sao os jogadores dos times participantes

### Janela de votacao
Periodo em que um palpite pode ser registrado ou alterado. Para palpite de vencedor de partida, fecha no inicio da partida. Para palpites de campeao, artilheiro e lider de assistencias, fecha no inicio do torneio.

### Apuracao do palpite
Processo automatico que ocorre apos a conclusao do evento alvo, quando o sistema compara o palpite registrado com o resultado real e classifica o palpite como acertado ou nao acertado.

### Percentual de palpites
Distribuicao agregada e anonima dos palpites registrados por opcao, exibida em tempo real enquanto a janela de votacao estiver aberta.

### Feed social do torneio
Espaco de comunicacao e interacao vinculado a um torneio. Reune comunicados oficiais, comentarios de usuarios autenticados e atualizacoes automaticas sobre jogos.

### Comunicado oficial
Publicacao feita pelo organizador do torneio para informar regras, horarios, mudancas ou avisos importantes.

### Comentario
Interacao textual feita por usuario autenticado em uma partida do torneio. Pode ser editada pelo proprio autor.

### Atualizacao automatica
Publicacao criada pelo sistema no feed social quando ocorre um evento relevante da partida, como resultado registrado.
