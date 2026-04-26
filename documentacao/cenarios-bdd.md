# Cenarios BDD

## Funcionalidades com cenarios definidos

### F1. Visualizar torneios disponiveis na plataforma
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/visualizar-torneios-disponiveis.feature

Cenarios principais:
- visualizar torneios disponiveis como visitante
- visualizar torneios disponiveis como usuario autenticado
- exibir mensagem quando nao houver torneios disponiveis

---

### F2. Permitir acesso autenticado as funcionalidades de criacao e gerenciamento de torneios
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/acesso-autenticado-gerenciamento-torneios.feature

Cenarios principais:
- permitir acesso as funcionalidades de criacao de torneio para usuario autenticado
- permitir acesso a area de gerenciamento de torneios para usuario autenticado
- impedir acesso as funcionalidades de criacao e gerenciamento para usuario nao autenticado

---

### F3. Solicitar participacao de um time em torneio aberto
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/solicitar-participacao-em-torneio-aberto.feature

Cenarios principais:
- solicitar participacao com sucesso quando o usuario possui time cadastrado
- impedir solicitacao quando o usuario nao possui time cadastrado
- impedir solicitacao quando o usuario nao esta autenticado
- impedir solicitacao quando o torneio nao aceita novas participacoes

---

### F4. Avaliar solicitacoes de participacao de times
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/avaliar-solicitacoes-de-participacao.feature

Cenarios principais:
- aprovar solicitacao de participacao de um time
- rejeitar solicitacao de participacao de um time
- impedir avaliacao por usuario que nao e organizador do torneio
- informar ausencia de solicitacoes pendentes para avaliacao

---

### F5. Gerenciar times do usuario
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/gerenciar-times-do-usuario.feature

Cenarios principais:
- criar um novo time com sucesso
- editar informacoes de um time do usuario
- excluir um time sem vinculo em torneio
- impedir exclusao de time vinculado a torneio

---

### F6. Gerenciar elenco de jogadores de um time
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/gerenciar-elenco-de-jogadores.feature

Cenarios principais:
- adicionar jogador ao elenco do time
- editar dados de um jogador do elenco
- remover jogador do elenco do time
- impedir gerenciamento do elenco por usuario nao responsavel

---

### F7. Gerenciar comissao tecnica de um time
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/gerenciar-comissao-tecnica.feature

Cenarios principais:
- associar tecnico a um time
- editar dados de um tecnico do time
- remover tecnico da comissao tecnica do time
- impedir gerenciamento da comissao tecnica por usuario nao responsavel

---

### F8. Vincular um time a um usuario responsavel
Dominio: dominio-participacao
Arquivo: dominio-participacao/src/test/resources/com/torneios/dominio/participacao/vincular-time-a-usuario-responsavel.feature

Cenarios principais:
- vincular um time a um usuario responsavel com sucesso
- alterar o responsavel de um time
- impedir vinculo de time a usuario inexistente
- impedir gerenciamento do time por usuario nao vinculado

---

### F9. Criar e configurar torneio
Dominio: dominio-torneio
Arquivo: dominio-torneio/src/test/resources/com/torneios/dominio/torneio/criar-e-configurar-torneio.feature

Cenarios principais:
- criar torneio com formato de competicao e formato de equipe validos
- impedir criacao de torneio sem formato de competicao
- impedir criacao de torneio sem definicao da quantidade de jogadores por equipe
- definir se o torneio sera aberto para solicitacao ou com participantes definidos

---

### F10. Gerenciar participantes aprovados antes do inicio do torneio
Dominio: dominio-torneio
Arquivo: dominio-torneio/src/test/resources/com/torneios/dominio/torneio/gerenciar-participantes-aprovados-do-torneio.feature

Cenarios principais:
- aprovar solicitacao de participacao de um time
- remover time aprovado antes do inicio do torneio
- impedir gerenciamento por usuario nao organizador
- impedir alteracao apos inicio do torneio

---

### F11. Gerar estrutura da competicao
Dominio: dominio-torneio
Arquivo: dominio-torneio/src/test/resources/com/torneios/dominio/torneio/gerar-estrutura-da-competicao.feature

Cenarios principais:
- gerar estrutura para torneio mata-mata
- gerar estrutura para pontos corridos
- gerar estrutura para fase de grupos
- impedir geracao sem participantes suficientes

---

### F12. Gerar partidas do torneio
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/gerar-partidas-do-torneio.feature

Cenarios principais:
- gerar partidas para pontos corridos
- gerar partidas para mata-mata
- gerar partidas para fase de grupos
- impedir geracao sem estrutura previa

---

### F13. Registrar resultado da partida
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/registrar-resultado-da-partida.feature

Cenarios principais:
- registrar resultado valido de uma partida
- registrar apenas o placar da partida sem informar eventos estatisticos
- atualizar automaticamente classificacao ou chaveamento apos resultado
- impedir registro por usuario nao organizador
- impedir registro para partida inexistente ou invalida

---

### F14. Atualizar e visualizar classificacao ou chaveamento da competicao
Dominio: dominio-competicao
Arquivo: dominio-competicao/src/test/resources/com/torneios/dominio/competicao/visualizar-classificacao-ou-chaveamento.feature

Cenarios principais:
- visualizar classificacao em torneio de pontos corridos
- visualizar chaveamento em torneio mata-mata
- exibir dados atualizados apos resultados
- impedir visualizacao antes da geracao da estrutura

---

### F15. Registrar eventos estatisticos opcionais da partida
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/registrar-eventos-estatisticos-da-partida.feature

Cenarios principais:
- registrar gols e assistencias de jogadores
- registrar cartoes amarelos e vermelhos
- impedir registro por usuario nao autorizado
- impedir registro para jogador que nao pertence aos times da partida
- permitir que o resultado da partida exista mesmo sem registro de eventos estatisticos

---

### F16. Calcular e visualizar estatisticas do torneio
Dominio: dominio-estatisticas
Arquivo: dominio-estatisticas/src/test/resources/com/torneios/dominio/estatisticas/calcular-e-visualizar-estatisticas.feature

Cenarios principais:
- calcular nota estatistica com base nos eventos registrados
- gerar ranking de artilharia do torneio
- visualizar estatisticas dos jogadores
- atualizar estatisticas apos novos eventos registrados
- exibir estatisticas apenas quando houver eventos registrados

---

## Regras de negocio cobertas

### Acesso e participacao
- RN01. Usuarios nao autenticados podem visualizar os torneios disponiveis.
- RN02. Apenas usuarios autenticados podem criar torneios.
- RN03. Apenas usuarios autenticados podem solicitar participacao em torneios.
- RN04. Usuario deve possuir time cadastrado para solicitar participacao.
- RN05. Torneio pode ser aberto ou fechado para participacao.
- RN06. Apenas organizador pode aprovar ou rejeitar solicitacoes.

### Organizacao do torneio
- RN07. Todo torneio deve possuir formato definido.
- RN08. Formatos validos: mata-mata, grupos + mata-mata, pontos corridos, final unica.
- RN09. Todo torneio possui organizador responsavel.
- RN10. Torneio so pode iniciar com participantes suficientes.
- RN11. Estrutura depende do formato.
- RN12. O torneio deve definir a quantidade de jogadores por equipe.
- RN13. As partidas devem respeitar a quantidade de jogadores definida.

### Times, jogadores e tecnicos
- RN14. Time so participa se aprovado.
- RN15. Jogador pertence a um time.
- RN16. Tecnico associado ao time participante.
- RN17. Apenas jogadores validos podem ter eventos registrados.

### Partidas e competicao
- RN18. Partida pertence a um torneio e dois times validos.
- RN19. Apenas partidas validas geram impacto no sistema.
- RN20. Resultado atualiza classificacao ou chaveamento automaticamente.
- RN21. Nao permitir resultados invalidos.
- RN22. O resultado da partida pode ser registrado sem eventos estatisticos.

### Estatisticas
- RN23. Registrar gols, assistencias e cartoes quando desejado.
- RN24. Nota estatistica calculada automaticamente quando houver eventos.
- RN25. Nota baseada em formula com pesos.
- RN26. Considera eventos basicos na versao inicial.
- RN27. Eventos positivos e negativos afetam a nota.
- RN28. Artilharia atualizada automaticamente quando houver gols registrados.
- RN29. Na ausencia de eventos, apenas o placar oficial da partida deve ser exibido.
