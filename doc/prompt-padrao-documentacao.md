# 📋 Prompt Reutilizável — Padrão de Documentação Spec-Driven (IA & Humanos)

Este arquivo contém o **prompt mestre** para replicar a arquitetura de documentação deste projeto (**Spec-Driven Development**) em qualquer outro projeto novo ou existente.

Ele foi desenhado para funcionar perfeitamente tanto para **desenvolvedores humanos** (leitura rápida, visual e navegável no GitHub/IDE) quanto para **agentes de IA** (contexto estruturado em camadas, IDs rastreáveis, grafos Mermaid e memória incremental por Card/Épico).

---

## 🚀 Como usar

1. Copie o bloco do **Prompt Principal** abaixo.
2. Preencha os campos entre colchetes (`[NOME DO PROJETO]`, `[DESCRIÇÃO / IDEIA]`, `[STACK TECNOLÓGICA]`) no final do prompt — ou anexe um rascunho de requisitos se já tiver.
3. Envie para a IA no início do projeto (ou em um projeto já em andamento para que ela estruture a pasta `doc/`).

---

## 🤖 1. Prompt Principal (Criar / Estruturar Documentação do Projeto)

````markdown
Atue como um Arquiteto de Software Sênior e Product Manager. Quero estruturar a documentação deste projeto seguindo o padrão **Spec-Driven Development (Desenvolvimento Guiado por Especificação)**, otimizado tanto para leitura humana quanto para contexto de Agentes de IA.

A documentação deve evitar "documentation drift" (divergência entre documentação e código), usar IDs rastreáveis (`RF-*`, `RNF-*`, `RN-*`, `CARD-*`), tabelas Markdown limpas e diagramas Mermaid nativos.

---

### 📂 Arquitetura de Arquivos Obrigatória

Você deve criar e manter a seguinte hierarquia de arquivos no repositório:

```text
projeto/
├── README.md                                      # Guia prático de execução, cURLs reais e tabela RBAC
├── http/
│   └── api-collection.json                        # Coleção Postman v2.1 atualizada a cada rota criada
└── doc/
    ├── gemini.md                                  # Governança da IA, Mapa da Documentação, Stack, Padrões e Git Flow
    ├── documento-de-requisitos.md                 # Visão completa do produto (Fases 1, 2 e 3), RFs, RNFs e RNs
    ├── SPEC.md                                    # Especificação Técnica VIVA do MVP, Mermaid, ER, Épicos/Cards e Rotas
    └── spec/                                      # Memória incremental: 1 arquivo por Card/Épico concluído
        ├── 01-card-01-modelagem-dados.md
        ├── 02-card-02-autenticacao.md
        └── XX-epico-YY-card-ZZ-nome-da-entrega.md
```

---

### 📐 Regras e Template de Cada Arquivo

#### 1. `doc/gemini.md` (Guia de Governança da IA e Padrões de Engenharia)
Deve conter:
- **Seção 0 — Mapa da Documentação (Ordem de Leitura):** Explicando a hierarquia (`gemini.md` → `SPEC.md` → `doc/spec/*` → `documento-de-requisitos.md` → `README.md`).
- **Seção 1 — Regras Obrigatórias da IA:**
  1. Nunca alterar código, infra ou testes sem autorização prévia (mostrar proposta antes e aguardar "de acordo").
  2. Nunca executar `git commit` ou `git push` sem autorização explícita.
  3. Atuar como revisor (avaliar SOLID, Clean Code, Complexidade Ciclomática).
  4. Atualização contínua: toda entrega deve criar/atualizar o arquivo correspondente em `doc/spec/`, marcar o status no `doc/SPEC.md` e atualizar `http/api-collection.json`.
  5. Fluxo guiado pelo usuário (Pair Programming Ativo).
- **Seção 2 — Stack & Arquitetura:** Tecnologias e versões exatas utilizadas no código real.
- **Seção 3 — Git Flow & Conventional Commits:** Topologia (`main`, `develop`, `feature/*`, `release/*`, `hotfix/*`) e padrão `<tipo>(<escopo>): <descrição>`.
- **Seções 4, 5 e 6 — Padrões de Código, Segurança e Estratégia de Testes:** (Package-by-feature, DTOs imutáveis, tratamento global de erros, testes de integração com Testcontainers/banco real).

---

#### 2. `doc/documento-de-requisitos.md` (Documento de Requisitos do Produto)
Visão de negócio completa (MVP + Fases Futuras). **Regra estrita de formatação:** Use exclusivamente tabelas Markdown válidas (`| Coluna | Coluna |`), sem quebras de linha soltas.
Estrutura obrigatória:
1. Visão Geral do Produto e Objetivos.
2. Escopo Inicial (O que faz parte vs. O que NÃO faz parte).
3. Perfis de Usuário (Tabela: `Perfil | Descrição | Permissões principais`).
4. Conceitos Principais / Glossário de Domínio (Tabela: `Conceito | Descrição`).
5. Requisitos Funcionais agrupados por módulo (Tabelas: `ID (RF-001...) | Requisito | Prioridade`).
6. Regras de Negócio detalhadas (`RN-001`, `RN-002`... com fórmulas e exemplos práticos).
7. Máquina de Estados das entidades principais (Tabela: `De | Para | Gatilho`).
8. Fluxo Principal e Fluxos de Exceção.
9. Requisitos de Dados / Entidades (Tabelas: `Campo | Tipo | Obrigatório | Descrição`).
10. Requisitos Não Funcionais agrupados por categoria: Usabilidade, Performance, Segurança, Disponibilidade, Privacidade (Tabelas: `ID (RNF-001...) | Requisito`).
11. Critérios de Aceite Gerais e Roadmap de Evolução (Fase 1 MVP, Fase 2 Consolidação, Fase 3 Expansão).

---

#### 3. `doc/SPEC.md` (Especificação Técnica do MVP — Fonte da Verdade de Engenharia)
Recorte técnico focado exclusivamente no escopo atual (MVP), referenciando o `documento-de-requisitos.md`.
Estrutura obrigatória:
1. **Visão Geral e Escopo do MVP:** O que o MVP cobre vs. o que fica para fases futuras.
2. **Stack Tecnológica:** Tabela (`Camada | Tecnologia | Versão`) refletindo exatamente o arquivo de build do projeto.
3. **Arquitetura do Sistema:**
   - Diagrama de Componentes em `mermaid` (`graph LR` ou `flowchart LR`).
   - Árvore de Organização de Pacotes (`infra/` e `domain/` package-by-feature) fiel ao código.
   - Explicação das camadas (`Controller → Service → Repository`, `DTO`, `Mapper`).
4. **Perfis e Controle de Acesso (RBAC):**
   - Matriz de Permissões em tabela (`Recurso | ADMIN | PERFIL_X | PERFIL_Y`) usando `✅` e `❌`, explicando regras de *Ownership* (dono do recurso).
5. **Épicos e Cards do MVP:**
   - Tabelas divididas por Épico contendo: `Card (CARD-01...) | Descrição | Requisitos (RF-*, RN-*) | Documentação (link para ./spec/...) | Status (✅ ou ⏳ Pendente)`.
6. **Modelo de Dados:**
   - Diagrama Entidade-Relacionamento completo em `mermaid` (`erDiagram`).
   - Tabela de Migrações (Flyway/Liquibase) com status (`✅`).
   - Tabela de Enums e seus valores exatos no código.
7. **Contratos de API (Alto Nível):**
   - Tabelas por módulo (`Método | Endpoint (com prefixo ex: /api/v1/...) | Descrição | Acesso`) e exemplo JSON da resposta padrão de erro (`GlobalExceptionHandler`).
8. **Regras de Negócio do MVP:**
   - Resumo técnico de cada `RN-*` indicando ao final: `**Implementa:** CARD-XX`.
9. **Máquinas de Estado e Fluxos Críticos:**
   - Diagramas `stateDiagram-v2` e `flowchart TD` em `mermaid`.
10. **Segurança e Decisões Técnicas (Trade-offs do MVP):**
    - Tabela de `RNF-*` de segurança com a solução técnica adotada e tabela de simplificações do MVP (`Decisão | Justificativa`).
11. **Critérios de Aceite do MVP (BDD):**
    - Agrupados por Épico no formato: **Dado** que [...], **quando** [...], **então** [...].

---

#### 4. `doc/spec/XX-epico-YY-card-ZZ-nome.md` (Documentação Incremental por Card/Épico)
Sempre que um Card ou Épico for concluído, crie/atualize seu arquivo dentro de `doc/spec/` seguindo este template padronizado:

```markdown
# [Emoji] Especificação Técnica: CARD-XX - [Título do Card/Épico]

**Status:** Concluído  
**Data de Conclusão:** DD/MM/AAAA  

## 1. Visão Geral
Resumo objetivo do que foi entregue nesta etapa e seu papel na arquitetura do sistema.

---

## 2. Requisitos & Regras de Negócio Atendidos
| Código | Descrição |
|:---|:---|
| **RF-XXX** | Descrição resumida de como foi atendido |
| **RN-XXX** | Descrição resumida da regra aplicada |

---

## 3. Endpoints Implementados
| Método | Rota | Descrição | Permissão | Status Retorno |
|:---|:---|:---|:---|:---|
| `POST` | `/api/v1/recurso` | Cria recurso | `ADMIN`, `PERFIL` | `201 Created` |

---

## 4. Componentes e Regras Implementadas
Lista objetiva das classes criadas/alteradas (DTOs, Mappers, Services, Validadores Strategy, Controllers, Mensageria, Exceptions) e regras de validação aplicadas.

---

## 5. Testes Automatizados (Integração / Unidade)
Lista numerada de todos os cenários testados na suíte de testes (ex: `RecursoControllerTest`), indicando o comportamento validado e o status HTTP esperado.
```

---

#### 5. `README.md` (Guia de Entrada do Repositório)
Deve estar 100% sincronizado com os DTOs e Controllers reais do código:
1. Badges de CI/CD e tecnologias.
2. Sumário navegável.
3. Diagrama Mermaid da pipeline de CI/CD e estratégia de branches.
4. Passo a passo de configuração local (`.env`, `docker compose up -d --build`, URLs dos serviços, Swagger UI e comando de testes).
5. Guia Passo a Passo da API com exemplos `cURL` e respostas JSON **idênticos aos campos reais dos DTOs**.
6. Tabela Consolidada de Endpoints e Permissões (RBAC).
7. Instruções para importar a coleção HTTP (`http/api-collection.json`).

---

### 🎯 Dados do Projeto Atual para Gerar a Documentação

- **Nome do Projeto:** [INSIRA O NOME DO PROJETO]
- **Objetivo / Descrição do Produto:** [DESCREVA O QUE O SISTEMA FAZ, QUEM SÃO OS USUÁRIOS E AS REGRAS PRINCIPAIS]
- **Stack Tecnológica:** [EX: Java 25, Spring Boot 4, PostgreSQL 16, Flyway, RabbitMQ, Docker, Testcontainers]
- **Escopo do MVP:** [LISTE O QUE DEVE ENTRAR NA FASE 1]

Antes de criar os arquivos, apresente o plano da estrutura e aguarde minha aprovação.
````

---

## 🔍 2. Prompt Auxiliar de Auditoria (Evitar *Documentation Drift*)

Use este prompt curto sempre que finalizar alguns cards ou quiser revisar se a documentação continua fiel ao código:

````markdown
Analise a pasta `doc/` (`gemini.md`, `documento-de-requisitos.md`, `SPEC.md` e todos os arquivos em `doc/spec/`) e o `README.md` da raiz, comparando-os com o código-fonte atual do projeto (Controllers, DTOs, Entities, Migrations e Testes).

Verifique e liste:
1. **Fidelidade Técnica (Drift):** Existem rotas, verbos HTTP, nomes de campos nos exemplos JSON, perfis RBAC ou versões de bibliotecas na documentação que divergem do código real?
2. **Progresso dos Cards:** Todos os cards já implementados em `doc/spec/` estão marcados como concluídos (`✅`) e com link na seção de Épicos do `doc/SPEC.md`?
3. **Padronização:** Todos os arquivos em `doc/spec/` possuem cabeçalho de `Status`/`Data`, tabela de requisitos atendidos, componentes criados e cenários de teste?
4. **Legibilidade (Humanos e IA):** Há tabelas quebradas, textos ambíguos ou diagramas Mermaid desatualizados?

Mostre um relatório objetivo das divergências encontradas e aguarde minha autorização para aplicar as correções.
````
