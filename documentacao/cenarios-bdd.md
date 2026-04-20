# Cenários BDD

## Funcionalidades com cenários definidos

### F1. Visualizar torneios disponíveis na plataforma
Arquivo: features/visualizar-torneios-disponiveis.feature

Cenários principais:
- visualizar torneios disponíveis como visitante.
- visualizar torneios disponíveis como usuário autenticado.
- exibir mensagem quando não houver torneios disponíveis.

---

### F2. Permitir acesso autenticado às funcionalidades de criação e gerenciamento de torneios
Arquivo: features/acesso-autenticado-gerenciamento-torneios.feature

Cenários principais:
- permitir acesso às funcionalidades de criação de torneio para usuário autenticado.
- permitir acesso à área de gerenciamento de torneios para usuário autenticado.
- impedir acesso às funcionalidades de criação e gerenciamento para usuário não autenticado.

---

### F3. Solicitar participação de um time em torneio aberto
Arquivo: features/solicitar-participacao-em-torneio-aberto.feature

Cenários principais:
- solicitar participação com sucesso quando o usuário possui time cadastrado.
- impedir solicitação quando o usuário não possui time cadastrado.
- impedir solicitação quando o usuário não está autenticado.
- impedir solicitação quando o torneio não aceita novas participações.

---

### F4. Avaliar solicitações de participação de times no torneio
Arquivo: features/avaliar-solicitacoes-de-participacao.feature

Cenários principais:
- aprovar solicitação de participação de um time.
- rejeitar solicitação de participação de um time.
- impedir avaliação por usuário que não é organizador do torneio.
- informar ausência de solicitações pendentes para avaliação.

---

### F5. Gerenciar times do usuário
Arquivo: features/gerenciar-times-do-usuario.feature

Cenários principais:
- criar um novo time com sucesso.
- editar informações de um time do usuário.
- excluir um time sem vínculo em torneio.
- impedir exclusão de time vinculado a torneio.

---

### F6. Gerenciar elenco de jogadores de um time
Arquivo: features/gerenciar-elenco-de-jogadores.feature

Cenários principais:
- adicionar jogador ao elenco do time.
- editar dados de um jogador do elenco.
- remover jogador do elenco do time.
- impedir gerenciamento do elenco por usuário não responsável.

---

### F7. Gerenciar comissão técnica de um time
Arquivo: features/gerenciar-comissao-tecnica.feature

Cenários principais:
- associar técnico a um time.
- editar dados de um técnico do time.
- remover técnico da comissão técnica do time.
- impedir gerenciamento da comissão técnica por usuário não responsável.

---

### F8. Vincular um time a um usuário responsável
Arquivo: features/vincular-time-a-usuario-responsavel.feature

Cenários principais:
- vincular um time a um usuário responsável com sucesso.
- alterar o responsável de um time.
- impedir vínculo de time a usuário inexistente.
- impedir gerenciamento do time por usuário não vinculado.

---

### F9. Criar e configurar torneio
Arquivo: features/criar-e-configurar-torneio.feature

Cenários principais:
- criar torneio com formato válido.
- impedir criação de torneio sem formato definido.
- criar torneio com vagas abertas para participação.
- criar torneio com participantes previamente definidos.

---

### F10. Gerenciar participantes aprovados do torneio
Arquivo: features/gerenciar-participantes-aprovados-do-torneio.feature

Cenários principais:
- aprovar solicitação de participação de um time.
- remover time aprovado antes do início do torneio.
- impedir gerenciamento por usuário não organizador.
- impedir alteração após início do torneio.

---

### F11. Gerar estrutura da competição
Arquivo: features/gerar-estrutura-da-competicao.feature

Cenários principais:
- gerar estrutura para torneio mata-mata.
- gerar estrutura para pontos corridos.
- gerar estrutura para fase de grupos.
- impedir geração sem participantes suficientes.

---

### F12. Gerar partidas do torneio
Arquivo: features/gerar-partidas-do-torneio.feature

Cenários principais:
- gerar partidas para pontos corridos.
- gerar partidas para mata-mata.
- gerar partidas para fase de grupos.
- impedir geração sem estrutura prévia.

---

### F13. Registrar resultado da partida
Arquivo: features/registrar-resultado-da-partida.feature

Cenários principais:
- registrar resultado válido de uma partida.
- atualizar automaticamente classificação ou chaveamento após resultado.
- impedir registro por usuário não organizador.
- impedir registro para partida inexistente ou inválida.

---

### F14. Atualizar e visualizar classificação ou chaveamento da competição
Arquivo: features/visualizar-classificacao-ou-chaveamento.feature

Cenários principais:
- visualizar classificação em torneio de pontos corridos.
- visualizar chaveamento em torneio mata-mata.
- exibir dados atualizados após resultados.
- impedir visualização antes da geração da estrutura.

---

### F15. Registrar eventos estatísticos da partida
Arquivo: features/registrar-eventos-estatisticos-da-partida.feature

Cenários principais:
- registrar gols e assistências de jogadores.
- registrar cartões amarelos e vermelhos.
- impedir registro por usuário não autorizado.
- impedir registro para jogador que não pertence aos times da partida.

---

### F16. Calcular e visualizar estatísticas do torneio
Arquivo: features/calcular-e-visualizar-estatisticas.feature

Cenários principais:
- calcular nota estatística com base nos eventos registrados.
- gerar ranking de artilharia do torneio.
- visualizar estatísticas dos jogadores.
- atualizar estatísticas após novos eventos registrados.

---

## Regras de negócio cobertas

### Acesso e participação
- RN01. Usuários não autenticados podem visualizar os torneios disponíveis.
- RN02. Apenas usuários autenticados podem criar torneios.
- RN03. Apenas usuários autenticados podem solicitar participação em torneios.
- RN04. Usuário deve possuir time cadastrado para solicitar participação.
- RN05. Torneio pode ser aberto ou fechado para participação.
- RN06. Apenas organizador pode aprovar ou rejeitar solicitações.

### Organização do torneio
- RN07. Todo torneio deve possuir formato definido.
- RN08. Formatos válidos: mata-mata, grupos + mata-mata, pontos corridos, final única.
- RN09. Todo torneio possui organizador responsável.
- RN10. Torneio só pode iniciar com participantes suficientes.
- RN11. Estrutura depende do formato.

### Times, jogadores e técnicos
- RN12. Time só participa se aprovado.
- RN13. Jogador pertence a um time.
- RN14. Técnico associado ao time.
- RN15. Apenas jogadores válidos podem ter eventos registrados.

### Partidas e competição
- RN16. Partida pertence a um torneio e dois times válidos.
- RN17. Apenas partidas válidas geram impacto no sistema.
- RN18. Resultado atualiza classificação automaticamente.
- RN19. Não permitir resultados inválidos.

### Estatísticas
- RN20. Registrar gols, assistências e cartões.
- RN21. Nota estatística calculada automaticamente.
- RN22. Nota baseada em fórmula com pesos.
- RN23. Considera eventos básicos na versão inicial.
- RN24. Eventos positivos e negativos afetam a nota.
- RN25. Artilharia atualizada automaticamente.