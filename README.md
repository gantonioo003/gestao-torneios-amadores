# Gestao de Torneios Amadores

Aplicacao web para criar, organizar e acompanhar torneios amadores de futebol. O projeto usa Spring Boot, Angular e MySQL.

## Abrir o projeto com Docker

### 1. Requisito

Instale e abra o [Docker Desktop](https://www.docker.com/products/docker-desktop/).

### 2. Subir a aplicacao

Na pasta raiz do projeto, execute:

```powershell
docker compose up --build -d
```

Na primeira execucao o Docker baixa as imagens e compila o frontend e o backend. Isso pode levar alguns minutos.

### 3. Acessar

- Aplicacao: http://localhost:4200
- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

### 4. Parar

```powershell
docker compose down
```

Para apagar tambem os dados do banco e recomecar do zero:

```powershell
docker compose down -v
```

## Comandos uteis

Ver se os containers estao ativos:

```powershell
docker compose ps
```

Ver os logs:

```powershell
docker compose logs -f
```

Recompilar depois de alterar o codigo:

```powershell
docker compose up --build -d
```

## Servicos

| Servico | Porta |
| --- | --- |
| Frontend Angular | `4200` |
| Backend Spring Boot | `8080` |
| MySQL | `3306` |

O Docker cria automaticamente o banco `torneios` com usuario e senha `torneios`.

## Estrutura principal

- `apresentacao-frontend`: interface Angular;
- `apresentacao-backend`: API Spring Boot;
- `aplicacao`: casos de uso;
- `dominio-*`: regras de negocio;
- `infraestrutura`: persistencia e integracoes;
- `documentacao`: requisitos, regras e cenarios BDD.

Os detalhes funcionais e de dominio ficam em [`documentacao/funcionalidades.md`](documentacao/funcionalidades.md) e [`documentacao/descricao-dominio.md`](documentacao/descricao-dominio.md).
