# pedido-crud-web — TP3

Sistema CRUD de Pedidos com interface web e API REST, desenvolvido em Java 21 / Spring Boot 3.2.3.

**Disciplina:** Engenharia Disciplinada de Software — INFNET

---

## Sumário

1. [Pré-requisitos](#pré-requisitos)
2. [Iniciar o sistema](#iniciar-o-sistema)
3. [Acessar a aplicação](#acessar-a-aplicação)
4. [Executar os testes](#executar-os-testes)
5. [Cobertura de código](#cobertura-de-código)
6. [Estrutura do projeto](#estrutura-do-projeto)
7. [Máquina de estados](#máquina-de-estados)
8. [API REST](#api-rest)
9. [Decisões de design](#decisões-de-design)

---

## Pré-requisitos

| Ferramenta     | Versão mínima                           |
|----------------|-----------------------------------------|
| Java (JDK)     | 21+                                     |
| Maven          | 3.9+                                    |
| Docker Desktop | 24+                                     |
| Google Chrome  | qualquer versão recente (para Selenium) |

---

## Iniciar o sistema

### Opção 1 — Tudo em Docker (banco + aplicação)

```bash
# Build e inicialização completa (3 containers)
docker compose up --build

# Acompanhar logs
docker compose logs -f app
```

### Opção 2 — Desenvolvimento local (banco Docker + app local)

```bash
# 1. Subir apenas o banco (porta 5433)
docker compose up db -d

# 2. Aguardar o container ficar healthy
docker compose ps

# 3. Iniciar a aplicação (perfil dev — DataLoader popula 50 pedidos de exemplo)
mvn spring-boot:run
```

### Parar tudo

```bash
docker compose stop
```

> Os dados são preservados no volume Docker `pedidos_data`.
> Para apagar os dados: `docker compose down -v`

---

## Acessar a aplicação

| Serviço              | URL                                      |
|----------------------|------------------------------------------|
| **Aplicação web**    | http://localhost:8082/pedidos            |
| **Swagger UI**       | http://localhost:8082/swagger-ui.html    |
| **Health check**     | http://localhost:8082/actuator/health    |
| **pgAdmin**          | http://localhost:5051                    |

**Credenciais pgAdmin:** `admin@admin.com` / `admin`
**Banco:** host `db`, porta `5432`, usuário `postgres`, senha `postgres`, db `pedidos_db`

---

## Executar os testes

### Testes unitários + fuzz (sem Docker)

Usam H2 in-memory — não precisam do banco rodando.

```bash
mvn test
```

Saída esperada:

```
Tests run: 18  — PedidoControllerTest       (MockMvc — interface HTML)
Tests run: 15  — PedidoRestControllerTest   (MockMvc — API REST)
Tests run: 29  — PedidoServiceTest          (Mockito)
Tests run: 40  — PedidoParametrizadoTest    (@ParameterizedTest)
Tests run: 12  — PedidoFalhaInfraTest       (timeout / rede / sobrecarga)
Tests run: 12  — PedidoServiceDTOTest       (métodos DTO do service)
Tests run:  9  — PedidoMapperTest           (MapStruct)
Tests run:  6  — PedidoFuzzTest             (Jqwik property-based)
───────────────────────────────────────────
BUILD SUCCESS  (141 testes no total, excluindo Selenium)
```

### Testes Selenium E2E (requer aplicação rodando)

```bash
# Em um terminal: iniciar a aplicação
mvn spring-boot:run

# Em outro terminal: executar Selenium
mvn test -Dtest=PedidoSeleniumTest
```

> O Chrome deve estar instalado. O WebDriverManager baixa o ChromeDriver automaticamente.

### Rodar uma classe específica

```bash
mvn test -Dtest=PedidoServiceTest
mvn test -Dtest=PedidoFuzzTest
mvn test -Dtest=PedidoFalhaInfraTest
mvn test -Dtest=PedidoRestControllerTest
```

---

## Cobertura de código

Após `mvn test`, o relatório JaCoCo é gerado em:

```
target/site/jacoco/index.html
```

```bash
# Abrir no Windows
start target\site\jacoco\index.html
```

### Resultado obtido

| Métrica           | Mínimo exigido | Resultado  |
|-------------------|----------------|------------|
| **LINE coverage** | 85%            | **92%** ✅ |
| Branch coverage   | —              | 93% ✅     |
| Method coverage   | —              | 100% ✅    |
| Class coverage    | —              | 100% ✅    |

### O que está excluído da cobertura

| Excluído                          | Motivo                                          |
|-----------------------------------|-------------------------------------------------|
| `Tp3Application.class`            | Bootstrap Spring Boot, sem lógica testável      |
| `br/com/infnet/dto/*.class`       | Apenas campos de dados com Lombok               |
| `br/com/infnet/config/*.class`    | Configuração trivial (`@EnableJpaAuditing`)     |
| Código Lombok (`@Generated`)      | Getters/setters/construtores gerados pelo Lombok|

---

## Estrutura do projeto

```
pedido-crud-web/
├── src/main/java/br/com/infnet/
│   ├── Tp3Application.java                   Ponto de entrada Spring Boot
│   ├── config/
│   │   ├── DataLoader.java                   Seed de 50 pedidos (perfil dev, idempotente)
│   │   └── JpaAuditingConfig.java            @EnableJpaAuditing
│   ├── domain/
│   │   ├── Pedido.java                       Entidade JPA (modelo anêmico)
│   │   ├── StatusPedido.java                 Enum com 5 estados
│   │   └── exception/
│   │       ├── DomainException.java          Exceção base do domínio
│   │       └── PedidoNaoEncontradoException.java
│   ├── dto/
│   │   ├── PedidoRequest.java                Entrada: criar/atualizar
│   │   ├── PedidoResponse.java               Saída: representação pública
│   │   └── ContestarRequest.java             Entrada: contestar pedido
│   ├── mapper/
│   │   └── PedidoMapper.java                 Interface MapStruct
│   ├── factory/
│   │   └── PedidoFactory.java                Construção de instâncias de Pedido
│   ├── repository/
│   │   └── PedidoRepository.java             Spring Data JPA + consultas customizadas
│   ├── service/
│   │   └── PedidoService.java                Validações, regras de negócio e máquina de estados
│   └── controller/
│       ├── PedidoController.java             Interface web Thymeleaf (/pedidos)
│       ├── PedidoRestController.java         API REST (/api/v1/pedidos)
│       ├── GlobalExceptionHandler.java       Erros HTML → redirect + flash message
│       └── RestExceptionHandler.java         Erros REST → RFC 7807 ProblemDetail
│
├── src/main/resources/
│   ├── application.properties                Perfil padrão (dev)
│   ├── application-dev.properties            PostgreSQL localhost:5433
│   ├── application-docker.properties         PostgreSQL hostname db:5432
│   ├── application-prod.properties           Variáveis de ambiente
│   └── templates/pedidos/
│       ├── list.html                         Dashboard com estatísticas por status
│       ├── detail.html                       Detalhe com timeline de status
│       └── form.html                         Formulário criar/editar
│
└── src/test/java/br/com/infnet/
    ├── factory/PedidoTestFactory.java        Helpers de construção para testes
    ├── service/
    │   ├── PedidoServiceTest.java            29 testes Mockito
    │   ├── PedidoParametrizadoTest.java      40 testes @ParameterizedTest
    │   ├── PedidoFalhaInfraTest.java         12 testes de falhas de infraestrutura
    │   └── PedidoServiceDTOTest.java         12 testes de métodos DTO
    ├── controller/
    │   ├── PedidoControllerTest.java         18 testes MockMvc (web)
    │   └── PedidoRestControllerTest.java     15 testes MockMvc (API REST)
    ├── mapper/PedidoMapperTest.java          9 testes de mapeamento
    ├── fuzz/PedidoFuzzTest.java              6 propriedades Jqwik (1.000+ iterações)
    └── selenium/PedidoSeleniumTest.java      12 testes E2E headless
```

---

## Máquina de estados

O ciclo de vida do pedido é controlado por `PedidoService.validarTransicao()`:

```
PENDENTE    → PROCESSANDO | CANCELADO
PROCESSANDO → CONCLUIDO   | CANCELADO
CONCLUIDO   → CONTESTADO
CONTESTADO  → PROCESSANDO | CANCELADO
CANCELADO   → (estado terminal)
```

| Estado Atual | Estados Permitidos              |
|--------------|---------------------------------|
| PENDENTE     | PROCESSANDO, CANCELADO          |
| PROCESSANDO  | CONCLUIDO, CANCELADO            |
| CONCLUIDO    | CONTESTADO                      |
| CONTESTADO   | PROCESSANDO, CANCELADO          |
| CANCELADO    | *(nenhum)*                      |

Transição inválida lança `DomainException("Transição inválida: X → Y")`.
Transição idempotente (mesmo → mesmo) retorna sem persistir.

---

## API REST

Base path: `/api/v1/pedidos`

| Método   | Endpoint                    | Descrição             | Status     |
|----------|-----------------------------|-----------------------|------------|
| `GET`    | `/`                         | Lista paginada        | `200`      |
| `GET`    | `/{id}`                     | Busca por ID          | `200`      |
| `POST`   | `/`                         | Cria pedido           | `201`      |
| `PUT`    | `/{id}`                     | Atualiza pedido       | `200`      |
| `POST`   | `/{id}/status`              | Avança status         | `200`      |
| `POST`   | `/{id}/contestar`           | Contesta pedido       | `200`      |
| `DELETE` | `/{id}`                     | Remove pedido         | `204`      |

Erros retornam **RFC 7807 ProblemDetail** com status `404`, `422`, `400` ou `500`.

Documentação interativa: `http://localhost:8082/swagger-ui.html`

---

## Decisões de design

### Modelo anêmico
`Pedido` é uma entidade JPA pura. Toda lógica de validação e transições de estado vive em `PedidoService`. A construção de instâncias é centralizada em `PedidoFactory`.

### Fail Early
`PedidoController.parsearValor()` converte a string de valor para `BigDecimal` antes de chamar o serviço. Entradas inválidas lançam `DomainException` imediatamente, sem propagar `NumberFormatException` pela pilha.

### Fail Gracefully
- **Web:** `GlobalExceptionHandler` redireciona para `/pedidos` com flash message amigável — stack trace nunca aparece na interface.
- **REST:** `RestExceptionHandler` retorna `ProblemDetail` com mensagem controlada — sem detalhes internos expostos.

### Seed de dados (DataLoader)
`DataLoader` popula 50 pedidos de exemplo ao iniciar no perfil `dev`. É idempotente: ignora o seed se o banco já possuir dados.

### Mockito — Subclass Mock Maker
`mockito-extensions/org.mockito.plugins.MockMaker` configura o Mockito para usar subclass mocking, garantindo compatibilidade com as versões do Spring Boot e Mockito utilizadas no projeto.

### Lombok e JaCoCo
`lombok.config` define `lombok.addLombokGeneratedAnnotation = true`, fazendo o JaCoCo ignorar getters, setters e construtores gerados na contagem de cobertura.
