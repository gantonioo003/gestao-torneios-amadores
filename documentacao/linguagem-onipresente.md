# Linguagem Onipresente

## Termos principais do dominio

### Usuario
Pessoa que utiliza o sistema com conta cadastrada. Para criar torneios, gerenciar times e participar de fluxos protegidos, o usuario precisa estar autenticado.

### Visitante
Pessoa sem login que pode acessar areas publicas da plataforma e registrar palpites identificados por sessao ou outro identificador tecnico.

### Conta de usuario
Cadastro que identifica o usuario na plataforma, contendo nome, email, senha e tipo de conta. Pode ser criada, editada e excluida pelo proprio usuario.

### Tipo de conta
Classificacao principal da conta dentro da plataforma. Pode ser jogador ou organizador.

### Conta de jogador
Conta usada por atleta que deseja acompanhar torneios, interagir no feed, registrar palpites, consultar estatisticas e buscar times para jogar.

### Conta de organizador
Conta usada por responsavel por time ou torneio para criar competicoes, gerenciar times, organizar participantes, registrar resultados e publicar comunicados.

### Login
Processo de autenticacao realizado com email e senha validos para liberar o acesso do usuario as funcionalidades protegidas.

### Usuario organizador
Usuario responsavel por criar e gerenciar um torneio.

### Torneio
Competicao de futebol amador criada por um organizador.

### Edicao do torneio
Ciclo especifico de realizacao de um torneio. Um mesmo torneio pode ter varias edicoes ao longo do tempo.

### Historico de edicao
Registro arquivado de uma edicao ja finalizada, preservando participantes, resultados e estatisticas antes de iniciar uma nova edicao.

### Repeticao de torneio
Acao de transformar um torneio finalizado em uma nova edicao, mantendo o historico anterior e reiniciando o ciclo competitivo.

### Formato de torneio
Estrutura do torneio. Pode ser:
- mata-mata
- fase de grupos + mata-mata
- pontos corridos
- final unica

### Formato de equipe
Define a quantidade de jogadores em campo por time em uma partida, como 3x3, 5x5, 7x7 ou 11x11.

### Candidatura de participacao
Fluxo realizado por um usuario responsavel por um time para tentar entrar em um torneio aberto. Inclui o envio da solicitacao, o acompanhamento do status e a possibilidade de cancelar a candidatura enquanto ela estiver pendente.

### Inscricao de participante
Fluxo completo que transforma uma candidatura de time em participante do torneio. Envolve solicitacao pelo responsavel, acompanhamento, cancelamento enquanto pendente, avaliacao pelo organizador e ajuste da lista final antes do inicio.

### Solicitacao de participacao
Registro criado dentro da candidatura para pedir a entrada de um time em um torneio aberto.

### Status da candidatura
Situacao atual da candidatura de participacao. Pode ser pendente, aprovada, rejeitada ou cancelada.

### Lista final de participantes
Conjunto de times aprovados pelo organizador para disputar o torneio antes do inicio da competicao.

### Chat privado
Canal de conversa direta entre dois usuarios autenticados da plataforma, liberado apenas apos uma solicitacao de conversa ser aprovada.

### Solicitacao de conversa
Pedido enviado por um usuario para iniciar uma conversa privada com outro usuario. Enquanto nao for aprovado, fica na aba de solicitados e nao permite troca de mensagens.

### Aba de solicitados
Area do chat em que o usuario recebe pedidos de conversa pendentes, podendo aprovar ou recusar cada um.

### Conversa aprovada
Conversa privada que teve a solicitacao aceita pelo destinatario e passou a permitir envio de mensagens.

### Mensagem privada
Texto enviado dentro de uma conversa aprovada. So pode ser enviado por participantes da conversa.

### Time
Equipe participante de um torneio, vinculada a um usuario responsavel.

### Desafio amistoso
Convite opcional feito por um time a outro para realizar uma partida fora de torneios oficiais da plataforma.

### Amistoso
Partida combinada entre dois times por meio de um desafio aceito. Pode ter data, local e resultado registrado no historico.

### Historico de amistosos
Registro dos amistosos disputados por um time, incluindo adversario, data, local e placar.

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

### Sorteio da competicao
Modo de preparacao em que o sistema distribui automaticamente os times aprovados na estrutura, rodadas e partidas.

### Montagem manual da competicao
Modo de preparacao em que o organizador escolhe manualmente a ordem ou distribuicao dos times aprovados antes da geracao das partidas.

### Escalacao
Configuracao opcional definida pelo responsavel do time ou pelo tecnico para uma partida especifica, contendo o esquema tatico, os jogadores titulares por posicao, os jogadores reservas e a distribuicao visual dos titulares em uma mesa tatica. Essa visualizacao nao altera as regras da partida nem exige simetria entre os times. A escalacao pode ser editada ate o inicio da partida e fica congelada apos esse momento.

### Mesa tatica
Representacao visual da escalacao em campo, gerada a partir do esquema tatico e dos titulares posicionados, para mostrar onde cada jogador inicia a partida.

### Esquema tatico
Distribuicao das posicoes dos jogadores titulares em campo, compativel com o formato de equipe do torneio. Exemplos: 4-4-2 ou 4-3-3 no 11x11; 2-1-1 no 5x5; 1-1-1 no 3x3.

### Posicao
Funcao tatica atribuida a cada jogador titular dentro do esquema tatico, como goleiro, defensor, meio-campista ou atacante.

### Titular
Jogador escalado para iniciar a partida em uma posicao definida pelo esquema tatico.

### Reserva
Jogador relacionado para a partida que nao inicia como titular, mas fica disponivel para compor a equipe conforme a necessidade do time.

### Scout estatistico da partida
Registro opcional dos eventos individuais e detalhados de uma partida, como gols, assistencias, cartoes e substituicoes. Pode ser criado, corrigido ou ajustado pelo organizador sem substituir o placar oficial da partida.

### Gol
Evento estatistico registrado quando um jogador marca para seu time em uma partida.

### Assistencia
Evento estatistico registrado quando um jogador contribui diretamente para um gol.

### Cartao amarelo
Evento estatistico de advertencia recebido por um jogador durante a partida.

### Cartao vermelho
Evento estatistico de expulsao recebido por um jogador durante a partida.

### Substituicao
Evento estatistico opcional que registra a troca de um jogador por outro durante a partida. Pode existir independentemente da mesa tatica, desde que os jogadores envolvidos pertencam aos times da partida.

### Artilharia
Ranking dos jogadores com maior numero de gols no torneio, calculado quando houver registro de gols.

### Nota estatistica
Pontuacao calculada automaticamente pelo sistema com base em uma formula que considera os eventos registrados para um jogador em uma partida. So existe quando houver eventos registrados.

### Consolidacao de estatisticas
Processo que agrupa os eventos registrados no scout da partida para atualizar notas, artilharia, lideres de assistencias e historico dos jogadores.

### Historico do jogador
Conjunto de eventos e desempenho acumulado de um jogador dentro do torneio.

### Comparativo de desempenho
Analise gerada pelo sistema para comparar dois jogadores ou dois times usando estatisticas registradas, historico de partidas com eventos e posicao relativa em rankings. Pode ser apenas temporaria ou salva pelo usuario para consulta posterior.

### Pontuacao comparativa
Valor calculado para apoiar a comparacao de desempenho, considerando eventos positivos, como gols e assistencias, e eventos negativos, como cartoes.

### palpite
Registro feito por um usuario autenticado ou visitante identificado expressando sua aposta sobre o desfecho de um evento do sistema. Pode ser de tipo: vencedor de partida, campeao do torneio, artilheiro do torneio ou lider de assistencias do torneio.

### Votante
Identificacao de quem registrou um palpite. Pode representar uma conta de usuario ou um visitante identificado pela sessao.

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

### Feed social geral
Linha do tempo publica da plataforma, semelhante a uma rede social, com postagens sobre jogos, times, torneios e peladas. Visitantes podem visualizar, mas apenas usuarios autenticados podem publicar ou interagir.

### Feed social do torneio
Recorte do feed vinculado a um torneio especifico. Reune comunicados oficiais, comentarios de usuarios autenticados e atualizacoes automaticas sobre jogos.

### Postagem social
Publicacao criada por usuario autenticado no feed geral, podendo conter texto, midia e hashtags.

### Hashtag
Marcador textual usado para agrupar publicacoes por assunto, torneio, time ou evento.

### Curtida
Interacao simples de usuario autenticado em uma publicacao do feed.

### Reacao
Interacao de usuario autenticado que expressa uma resposta mais especifica a uma publicacao, como comemoracao ou surpresa.

### Comunicado oficial
Publicacao feita pelo organizador do torneio para informar regras, horarios, mudancas ou avisos importantes.

### Comentario
Interacao textual feita por usuario autenticado em uma partida do torneio. Pode ser editada pelo proprio autor.

### Atualizacao automatica
Publicacao criada pelo sistema no feed social quando ocorre um evento relevante da partida, como resultado registrado.

