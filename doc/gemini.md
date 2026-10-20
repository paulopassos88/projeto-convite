# 🪐 Skill Antigravity: Guia Unificado de Desenvolvimento

> **Versão:** 3.1 | **Foco:** Spring Boot 4.1+ (Java 25), Git Flow Seguro e Qualidade de Código.
> Este documento consolida as diretrizes de arquitetura, fluxo de trabalho e regras de interação para a IA.

---

## 🗺️ 0. Mapa da Documentação (Ordem de Leitura Recomendada)

Para entender o projeto rapidamente (seja você um desenvolvedor humano ou um agente de IA), siga esta hierarquia:

1. **[`doc/gemini.md`](./gemini.md) (Este arquivo):** Regras obrigatórias de interação da IA, padrões de código, arquitetura e Git Flow.
2. **[`doc/SPEC.md`](./SPEC.md):** Especificação técnica consolidada do **MVP (Fase 1)** — arquitetura, modelo de dados, contratos de rotas, regras de negócio e status dos Épicos/Cards.
3. **[`doc/spec/`](./spec/):** Histórico incremental detalhado do que já foi implementado e testado em cada Card/Épico (`01-card-01...` até o card atual).
4. **[`doc/documento-de-requisitos.md`](./documento-de-requisitos.md):** Visão completa do produto (Fases 1, 2 e 3), todos os Requisitos Funcionais (`RF-*`), Não Funcionais (`RNF-*`) e Regras de Negócio (`RN-*`).
5. **[`README.md`](../README.md) e [`http/api-collection.json`](../http/api-collection.json):** Guia prático de execução local (Docker/Gradle), exemplos de chamadas `cURL` e coleção Postman atualizada.

---

## 🤖 1. Regras de Interação da IA (Obrigatório)

1. **Autorização Prévia (Código e Infra):** 
   - **NUNCA** escreva, altere ou exclua código, configurações (como Docker, `.env`, etc.) ou testes sem autorização explícita do usuário.
   - **REGRA DE OURO:** Antes de implementar qualquer código ou arquivo, mostre EXATAMENTE o que vai ser feito (ex: o conteúdo do `docker-compose.yml`, `Dockerfile`, classes Java, etc.) e aguarde o "de acordo".
2. **Autorização Prévia (Git):** 
   - **NUNCA** execute `git commit` ou `git push` sem autorização explícita.
3. **Postura de Revisor:** 
   - Ao ler código, avalie mentalmente Complexidade Ciclomática, Code Smells e SOLID. Sugira refatorações explicando o "porquê", mas aguarde aprovação para reescrever.
4. **Cautela com Críticos:** 
   - Pare e pergunte antes de modificar configurações de segurança (`SecurityFilterChain`) ou transações globais.
5. **Testes:** 
   - Só implemente testes se o usuário solicitar explicitamente.
6. **Entregáveis:** 
   - Sempre inclua imports, explique anotações novas e, ao sugerir migrações de banco, inclua scripts Flyway.
7. **Atualização Contínua de Documentação:**
   - Toda alteração relevante para o contexto do projeto deve gerar uma atualização nos arquivos de especificação (dentro do diretório `doc/spec/` e no status do `doc/SPEC.md`) documentando a etapa.
8. **Atualização Contínua das Coleções HTTP:**
   - Toda alteração ou criação de rotas HTTP na API deve ser refletida atualizando o arquivo [`http/api-collection.json`](../http/api-collection.json) (formato Postman v2.1), garantindo compatibilidade com qualquer API Client (Postman, Insomnia, Bruno, etc.).
9. **Fluxo Guiado pelo Usuário (Pair Programming Ativo):**
   - O usuário liderará o desenvolvimento etapa por etapa. A IA atuará como co-piloto executando ordens granulares. A IA só deve assumir a liderança e sugerir/implementar os próximos passos se o usuário explicitamente pedir ajuda ou "travar".

---

## 🛠 2. Stack & Arquitetura

- **Core:** Spring Boot 4.1.0 (Java 25). **Atenção:** Use sempre `jakarta.*`, nunca `javax.*`.
- **Build:** Gradle (Groovy DSL — `api-convite/build.gradle`).
- **Dados:** PostgreSQL 16 + Spring Data JPA (Hibernate) + Flyway (`db/migration/V1__..V6__`).
- **Mensageria:** RabbitMQ 3 + Spring AMQP (disparo assíncrono de e-mails).
- **Mapeamento:** MapStruct 1.5.5 + Lombok.
- **API:** RESTful (`/api/v1`), JSON, OpenAPI 3.0 / Swagger UI (`springdoc-openapi`).
- **Segurança:** Spring Security 6 + JWT stateless (`jjwt 0.11.5`). *(Decisão: Evita Keycloak para economizar RAM em VPS e manter controle total da entidade `Usuario`)*.
- **Monitoramento:** Spring Boot Actuator integrado a Prometheus e Grafana.

---

## 🌳 3. Git Flow & Versionamento

### 🛑 A Regra de Ouro
> **NUNCA faça commits diretos na `main` ou `master`.** Toda alteração chega via Pull Request (PR).
> **Só fazer commits quando for solicitado.**

### Topologia Simplificada
- `main`: Espelho da produção estável.
- `develop`: Branch de integração contínua.
- `feature/*` e `bugfix/*`: Nascem e morrem em `develop`.
- `release/*`: Nasce em `develop`, merge para `main` e volta para `develop`.
- `hotfix/*`: Nasce em `main`, merge para `main` (com TAG) e volta para `develop`.

### Convenção de Commits (Conventional Commits)
- **Formato:** `<tipo>(<escopo>): <descrição imperativa e curta>`
- **Tipos:** `feat` (MINOR), `fix`/`perf` (PATCH), `docs`, `style`, `refactor`, `test`, `chore`.
- **Regras:** Commits atômicos, máx. 72 caracteres na 1ª linha. Nunca commite `.env` ou `node_modules`.

### Pull Requests
- Exige CI verde, cobertura ≥ 80%, sem conflitos e ao menos 1 aprovação.
- Deve conter: O quê, Porquê, Ticket (ANT-XXXX) e Como testar.

---

## 💻 4. Qualidade de Código & Padrões

### Estrutura e Design
- **Arquitetura:** Package-by-feature + Camadas (Controller → Service → Repository).
- **Princípios:** SOLID, DRY, alta coesão e baixo acoplamento.
- **Clean Code:** Baixa complexidade ciclomática. Máximo de **2 níveis de aninhamento** (`if/for`). Use *Early Returns*.

### Boas Práticas Spring
- **Injeção:** Via construtor (`@RequiredArgsConstructor`).
- **DTOs:** **NUNCA** exponha `@Entity` no controller. Use `record` para DTOs.
- **Validação:** `@Valid` no controller + Bean Validation (`@NotBlank`, `@Email`, etc.) nos DTOs.
- **Erros:** Tratamento global via `@RestControllerAdvice` + `ErrorResponse` padronizado.
- **Logging:** `@Slf4j` (JSON em produção). Nunca use `System.out.println`.

---

## 🔒 5. Segurança

- **Secrets:** Variáveis de ambiente ou `application-{profile}.yml` (nunca commitar).
- **Configuração:** `@Bean SecurityFilterChain` (stateless para APIs).
- **Autorização:** `@EnableMethodSecurity` + `@PreAuthorize("hasRole('ADMIN')")`.
- **CORS e Inputs:** Configuração explícita no `WebSecurityConfig` e validação rigorosa de headers/params.

---

## 🧪 6. Estratégia de Testes

*(Lembrete: Só implementar se autorizado)*

- **Frameworks:** JUnit 5, Mockito, Testcontainers (para DB/Redis/Kafka).
- **Padrão:** AAA (Arrange, Act, Assert).
- **Cobertura:** Foco em *branch coverage* (preparando para JaCoCo).
- **Cenários Obrigatórios:** Happy path, validações de erro, 404, 401/403, e rollback de transação.
- **Integração:** `@SpringBootTest` + `@AutoConfigureMockMvc` + `@Transactional` (rollback automático).

---

## 📚 7. Referências e Links Úteis
- **Git Flow:** [Modelo Original (nvie)](https://nvie.com/posts/a-successful-git-branching-model/)
- **Commits:** [Conventional Commits 1.0](https://www.conventionalcommits.org/)
- **Versionamento:** [Semantic Versioning 2.0](https://semver.org/)