# Épico 2: Gestão de Eventos

**Status:** Concluído
**Data de Conclusão:** 31/08/2026

## Objetivo
Implementar o fluxo completo de criação, listagem e administração de Eventos na API. Os eventos são a entidade central do sistema, pois é a partir deles que os convites e o check-in na portaria (Épico 3) funcionarão.

## O que foi implementado

### 1. DTOs e Mapeamento (MapStruct)
- `CriarEventoDTO`: Validações rigorosas de campos obrigatórios (`@NotBlank`), datas futuras (`@Future`, `@FutureOrPresent`) e limites não negativos (`@Min(0)`).
- `AtualizarEventoDTO`: Suporte a alteração de campos e status do evento (`RASCUNHO`, `ATIVO`, `CANCELADO`, `ENCERRADO`).
- `EventoResponseDTO`: DTO de retorno omitindo dados sensíveis e expondo `organizadorNome`.
- `EventoMapper`: Conversões MapStruct com regras de segurança ignorando campos protegidos (`id`, `organizador`, `status`, `criadoEm`, `atualizadoEm`).

### 2. Infraestrutura de Segurança e Auditoria
- `UsuarioLogadoService`: Centralizado em `infra/security`, extrai o `Usuario` ou `UUID` autenticado diretamente do `SecurityContextHolder`.
- `MDC Logging`: `SecurityFilter` preenche o `usuarioEmail` no MDC com limpeza garantida em `finally`, exibindo o e-mail do autor da ação em todos os logs de console.

### 3. Validações e Regras de Negócio
- `ValidadorCriacaoEvento` (Strategy Pattern):
  - `ValidadorDataTerminoPosteriorInicio`: Impede que a data de término seja anterior ou igual ao início.
  - `ValidadorDataInicioFutura`: Impede criar eventos no passado.
- `EventoNaoEncontradoException`: Exceção personalizada mapeada para HTTP 404 no `GlobalExceptionHandler`.
- `EventoService`:
  - `criar`: Executa validadores, associa o organizador autenticado e define status `ATIVO`.
  - `listar`: Aplica controle de acesso (ADMIN enxerga todos; ORGANIZADOR enxerga apenas os seus) com paginação e ordenação.
  - `buscarPorId`, `atualizar`, `cancelar`: Validam se o usuário é dono ou ADMIN (lança `AccessDeniedException` / 403 para terceiros).

### 4. Controller e Rotas REST
- `EventoController` mapeado em `/eventos` (prefixo global `/api/v1/eventos`):
  - `POST /api/v1/eventos` -> HTTP 201 Created com header `Location`
  - `GET /api/v1/eventos` -> HTTP 200 OK (Page)
  - `GET /api/v1/eventos/{id}` -> HTTP 200 OK
  - `PUT /api/v1/eventos/{id}` -> HTTP 200 OK
  - `DELETE /api/v1/eventos/{id}` -> HTTP 204 No Content
  - Todos protegidos por `@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")`.

### 5. Coleção HTTP (Postman)
- Arquivo `http/api-collection.json` na raiz, com variáveis de coleção (`baseUrl`, `token`, `eventoId`) e scripts automáticos de extração de token e id de evento.

### 6. Testes de Integração (Testcontainers)
- `EventoControllerTest`: 8 testes cobrindo todo o ciclo de vida do evento:
  - Criação de evento com sucesso por `ORGANIZADOR` com preenchimento automático do dono via JWT.
  - Bloqueio por validação de datas (HTTP 400).
  - Bloqueio de acesso anônimo (HTTP 403).
  - Listagem com isolamento de visibilidade por perfil (ADMIN vs ORGANIZADOR).
  - Bloqueio de acesso/modificação de evento de outro organizador (HTTP 403).
  - Atualização e cancelamento bem-sucedidos pelo próprio organizador.
