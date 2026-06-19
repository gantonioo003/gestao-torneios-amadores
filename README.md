# Gestao de Torneios Amadores

Aplicacao web para criar, organizar e acompanhar torneios amadores de futebol. O projeto usa Spring Boot, Angular e MySQL.

## Como rodar o projeto

### 1. Instalar o Docker Desktop

Baixe e abra o [Docker Desktop](https://www.docker.com/products/docker-desktop/) antes de continuar.

### 2. Abrir o terminal na pasta raiz do projeto

No VS Code, abra o terminal integrado (`Ctrl + '`) e certifique-se de que voce esta na raiz do projeto (onde fica o arquivo `docker-compose.yml`).

### 3. Subir o banco, o backend e o frontend

```powershell
docker compose up --build -d
```

Na primeira execucao o Docker baixa as imagens e compila tudo. Isso pode levar alguns minutos.

### 4. Verificar se os containers subiram

```powershell
docker compose ps
```

Os tres servicos devem aparecer com status `running`: `torneios-mysql`, `torneios-backend` e `torneios-frontend`.

### 5. Acessar a aplicacao

- **Frontend:** http://localhost:4200
- **Backend (API):** http://localhost:8080
- **Swagger:** http://localhost:8080/swagger-ui.html

### 6. Ver os logs em tempo real (opcional)

```powershell
docker compose logs -f
```

Pressione `Ctrl + C` para sair dos logs sem derrubar os containers.

### 7. Parar a aplicacao

```powershell
docker compose down
```

Para apagar tambem os dados do banco e comecar do zero:

```powershell
docker compose down -v
```

### Recompilar apos alterar o codigo

```powershell
docker compose up --build -d
```

## Rodar localmente (sem Docker)

### Requisitos

- Java 21
- Maven 3.9+
- Node.js 22+
- MySQL 8 rodando em `localhost:3306`

### 1. Compilar todos os modulos (na raiz do projeto)

```powershell
mvn install -DskipTests
```

### 2. Subir o backend (em um terminal)

```powershell
cd apresentacao-backend
mvn spring-boot:run
```

O backend sobe em http://localhost:8080

### 3. Subir o frontend (em outro terminal)

```powershell
cd apresentacao-frontend/src/main/angular
npm install
npx ng serve --no-hmr
```

O frontend sobe em http://localhost:4200

### Usuarios de teste

| Email | Senha | Tipo |
| --- | --- | --- |
| `usuario@torneios.com` | `usuario_1` | ORGANIZADOR |
| `teste2@gmail.com` | `tec` | TREINADOR |
| `teste1@gmail.com` | `Teste1` | ORGANIZADOR |

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
