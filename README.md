# 🎟️ Projeto Convite - Gestor de Eventos e Convites

[![CI - Build e Testes de Integração](https://github.com/paulopassos88/projeto-convite/actions/workflows/ci.yml/badge.svg)](https://github.com/paulopassos88/projeto-convite/actions/workflows/ci.yml)
[![CD - Docker Publish](https://github.com/paulopassos88/projeto-convite/actions/workflows/cd.yml/badge.svg)](https://github.com/paulopassos88/projeto-convite/actions/workflows/cd.yml)
![Java](https://img.shields.io/badge/Java-25-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.0-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Multi--Stage-blue?logo=docker)
![Testcontainers](https://img.shields.io/badge/Testcontainers-Postgres-black?logo=testcontainers)

MVP de uma API RESTful para gestão de eventos, convidados e confirmações de presença (RSVP), construída com arquitetura moderna e boas práticas de engenharia de software e automação.

---

## 🚀 Tecnologias e Ferramentas

* **Linguagem & Framework:** Java 25, Spring Boot 4.1.0
* **Banco de Dados & Migrations:** PostgreSQL 16, Flyway
* **Segurança & Autenticação:** Spring Security, JWT (jjwt)
* **Mensageria:** Spring Boot AMQP (RabbitMQ)
* **Testes Automatizados:** JUnit 5, Spring Boot Test, **Testcontainers** (PostgreSQL real em contêiner)
* **Containerização:** Docker (Dockerfile Multi-Stage) e Docker Compose
* **CI/CD:** GitHub Actions (Integração Contínua com Testcontainers + Entrega Contínua para GitHub Packages / GHCR)

---

## ⚙️ Pipeline de CI/CD e Estratégia de Branches

O projeto segue um fluxo baseado em **GitFlow simplificado**:

```mermaid
flowchart LR
    Feature[feat/minha-feature] -->|Pull Request| Develop[develop]
    Develop -->|Testes Validados / Release| Main[main]
    
    subgraph CI["GitHub Actions (CI)"]
        PR_Check[Build Gradle + Testcontainers]
    end
    
    subgraph CD["GitHub Actions (CD)"]
        Docker_Push[Build Multi-Stage + Publicação GHCR]
    end
    
    Feature -.->|Aciona| PR_Check
    Develop -.->|Aciona| PR_Check
    Main -.->|Aciona no Merge| Docker_Push
```

* **Branch `develop`:** Ambiente de integração contínua onde as features são reunidas e validadas.
* **Branch `main`:** Versão estável de produção. Cada merge na `main` dispara automaticamente o pipeline de CD.
* **CI com Testcontainers:** Todos os testes de integração sobem contêineres Docker reais de banco de dados automaticamente no runner do GitHub Actions antes da aprovação do código.

---

## 📦 Como Executar Localmente

### Pré-requisitos
* Java 25 (JDK)
* Docker e Docker Compose

### 1. Clonar o repositório
```bash
git clone https://github.com/paulopassos88/projeto-convite.git
cd projeto-convite
```

### 2. Subir o ambiente com Docker Compose
```bash
docker compose up -d
```

### 3. Rodar os testes de integração localmente
```bash
cd api-convite
./gradlew check
```

---

## 📄 Documentação da API (Swagger / OpenAPI)
Com a aplicação em execução, a documentação interativa pode ser acessada em:
* `http://localhost:8080/api/v1/swagger-ui/index.html` (ou `/swagger-ui.html`)
