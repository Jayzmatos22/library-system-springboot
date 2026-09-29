<div align="center">

# 📚 Library System — Spring Boot Study

**Um sistema de biblioteca construído do zero para explorar o ecossistema Spring Boot na prática.**

[![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-latest-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Status](https://img.shields.io/badge/status-em_desenvolvimento-yellow?style=for-the-badge)](#-roadmap)

</div>

---

## 🎯 Sobre o projeto

Este repositório **não é um produto** — é um **laboratório de estudos**.

A ideia é usar um domínio familiar (uma biblioteca: livros, usuários e empréstimos) como pretexto para
percorrer, camada por camada, as peças que realmente aparecem em aplicações Spring Boot de produção:
persistência com JPA, validação de entrada, tratamento centralizado de erros, segurança, migrations
versionadas e documentação automática de API.

Cada commit representa um passo isolado de aprendizado, seguindo
[Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/) — dá para ler o histórico do
repositório como uma trilha de estudo.

## 🧠 O que está sendo estudado

| Tema | Ferramenta | O que se aprende na prática |
|------|-----------|------------------------------|
| **Inversão de controle** | Spring Core | Injeção via construtor com `@RequiredArgsConstructor`, em vez de `@Autowired` em campo |
| **Camada web** | Spring MVC | `@RestController`, mapeamento de rotas, `ResponseEntity` e status HTTP corretos |
| **Persistência** | Spring Data JPA | Entidades, `@Enumerated`, derived queries (`findByEmail`) e o contrato do `JpaRepository` |
| **Validação** | Bean Validation | `@NotBlank`, `@Email`, `@Size` em DTOs `record`, com mensagens de erro próprias |
| **Tratamento de erros** | `@RestControllerAdvice` | Exceções de domínio traduzidas em respostas JSON padronizadas |
| **Segurança** | Spring Security | `PasswordEncoder` para hash de senha — nunca texto puro no banco |
| **Migrations** | Flyway | Evolução do schema versionada, em vez de `ddl-auto: update` |
| **Documentação** | SpringDoc OpenAPI | Swagger UI gerado a partir do próprio código |
| **Ambiente** | Docker Compose | PostgreSQL provisionado automaticamente no boot da aplicação |
| **Boilerplate** | Lombok | Getters, setters e construtores gerados em tempo de compilação |

## 🏛️ Arquitetura

Organização em **camadas**, com as dependências sempre apontando para dentro:

```
HTTP  →  Controller  →  Service  →  Repository  →  PostgreSQL
              ↓            ↓
             DTO       Exception
              ↓            ↓
    Bean Validation    GlobalExceptionHandler  →  ErrorResponse (JSON)
```

O ponto central: **a entidade JPA nunca cruza a fronteira HTTP**. A entrada chega como
`RegisterUserDTO` (já validado) e a saída sai como `RegisterResponseDTO`. Isso evita vazar o campo
`password` e desacopla o contrato da API do modelo de dados.

### Estrutura de pacotes

```
src/main/java/com/springBootStudy/study/
├── StudyApplication.java            # entrypoint @SpringBootApplication
├── controller/                      # camada de entrada HTTP
│   └── UserController.java
├── service/                         # regras de negócio
│   ├── user/UserService.java        # cadastro + hash de senha + e-mail único
│   └── book/BookService.java
├── repository/                      # abstração de persistência (Spring Data)
│   ├── UserRepository.java
│   └── BookRepistory.java
├── model/                           # entidades JPA
│   ├── User.java
│   ├── Book.java
│   └── LoanBook.java                # regra de atraso (isLate)
├── dtos/user/                       # contratos de entrada/saída
│   ├── RegisterUserDTO.java
│   ├── RegisterResponseDTO.java
│   └── ErrorResponse.java
├── enums/
│   └── BookStatus.java
└── exceptions/
    ├── api/                         # exceções de domínio
    │   ├── EmailAlreadyExistsException.java
    │   └── UserExceptions.java
    └── globalAdvice/
        └── GlobalExceptionHandler.java
```

## 📖 Domínio

### `Book` — o acervo

Cada livro carrega título, autor, ISBN e um **estado** modelado como enum, em vez de `String` solta ou
flag booleana:

```java
public enum BookStatus {
    DISPONIVEL, EMPRESTADO, RESERVADO, EM_MANUTENCAO, PERDIDO
}
```

Persistido com `@Enumerated(EnumType.STRING)` — o banco guarda `"DISPONIVEL"`, não o ordinal `0`.
Assim, reordenar o enum no futuro não corrompe os dados já gravados.

### `User` — quem empresta

E-mail com constraint `unique` no banco **e** verificação no service antes de salvar. A senha nunca é
persistida em texto puro: passa por `PasswordEncoder`, e a coluna é dimensionada em 72 caracteres — o
limite de entrada do BCrypt.

### `LoanBook` — o empréstimo

Guarda três datas (`loanDate`, `dueDate` e `returnDate`) e concentra a regra de atraso no próprio
modelo:

```java
@Transient
public boolean isLate() {
    if (returnDate != null) {
        return returnDate.isAfter(dueDate);   // devolveu depois do prazo?
    }
    return LocalDate.now().isAfter(dueDate);  // ainda não devolveu e o prazo já passou?
}
```

`@Transient` é o detalhe que importa: atraso é um estado **derivado**, calculado na hora da consulta.
Não vira coluna no banco e, por isso, nunca fica dessincronizado da realidade.

## 🚀 Como rodar

### Pré-requisitos

- **JDK 21+**
- **Docker** com Docker Compose (o PostgreSQL sobe automaticamente)

### Passo a passo

```bash
# 1. clonar
git clone https://github.com/Jayzmatos22/library-system-springboot.git
cd library-system-springboot

# 2. subir a aplicação — o Spring Boot Docker Compose
#    inicia o Postgres do compose.yaml sozinho
./mvnw spring-boot:run
```

No Windows (PowerShell ou CMD), troque `./mvnw` por `mvnw.cmd`.

Graças à dependência `spring-boot-docker-compose`, **não é preciso rodar `docker compose up`
manualmente** nem preencher `spring.datasource.*`: o Spring detecta o `compose.yaml`, sobe o
container e injeta as credenciais de conexão.

### Comandos úteis

```bash
./mvnw compile        # compilar
./mvnw test           # rodar os testes
./mvnw clean package  # gerar o JAR executável em target/
```

### Documentação da API

Com a aplicação rodando, o Swagger UI fica disponível em:

```
http://localhost:8080/swagger-ui.html
```

## 🗺️ Roadmap

Estudo em andamento — o caminho planejado daqui para frente:

- [x] Scaffold do projeto + Maven wrapper
- [x] Entidades do domínio (`User`, `Book`, `LoanBook`) e `BookStatus`
- [x] Repositórios Spring Data JPA
- [x] DTOs com Bean Validation
- [x] Hash de senha com `PasswordEncoder`
- [x] Tratamento global de exceções (`@RestControllerAdvice`)
- [ ] Migrations Flyway para o schema inicial
- [ ] CRUD completo de livros
- [ ] Fluxo de empréstimo e devolução atualizando `BookStatus`
- [ ] Autenticação com JWT + `SecurityFilterChain`
- [ ] Testes de integração com Testcontainers
- [ ] Paginação e filtros na listagem do acervo
- [ ] Cobertura de testes ≥ 80%

## 🧰 Stack completa

| Camada | Tecnologia |
|--------|-----------|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 |
| Web | Spring MVC + RestClient |
| Persistência | Spring Data JPA (Hibernate) |
| Banco | PostgreSQL |
| Migrations | Flyway |
| Segurança | Spring Security |
| Validação | Jakarta Bean Validation |
| Docs | SpringDoc OpenAPI (Swagger UI) |
| Boilerplate | Lombok |
| Build | Maven (wrapper incluído) |
| Ambiente | Docker Compose |

## 📌 Convenção de commits

O histórico segue [Conventional Commits](https://www.conventionalcommits.org/pt-br/v1.0.0/):

| Tipo | Uso |
|------|-----|
| `feat` | nova funcionalidade |
| `fix` | correção de bug |
| `refactor` | mudança interna sem alterar comportamento |
| `test` | testes |
| `docs` | documentação |
| `build` | dependências e build |
| `chore` | infraestrutura e configuração |

---

<div align="center">

**Feito por [Jailton Santos](https://github.com/Jayzmatos22)** — estudando Spring Boot na prática, um commit por vez. 🌱

</div>
