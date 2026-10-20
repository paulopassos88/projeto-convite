# Épico 3: Gestão de Convidados

**Status:** Concluído (CARD-05 e CARD-06)  
**Data de Conclusão:** 02/09/2026  

## Objetivo
Implementar o fluxo completo de cadastro manual, busca, edição, remoção e controle de convidados associados a eventos na API, garantindo validações de duplicidade de e-mail por evento, integridade referencial, paginação e segurança via RBAC e controle de propriedade (Ownership).

---

## O que foi implementado

### 1. DTOs e Mapeamento (MapStruct)
- `CriarConvidadoDTO`: Validações de campos obrigatórios (`@NotBlank`), formato de e-mail (`@Email`), limite não negativo de acompanhantes (`@Min(0)` e `@NotNull`) e tamanho máximo de telefone.
- `AtualizarConvidadoDTO`: Permite atualização granular de dados cadastrais mantendo a integridade do `eventoId`.
- `ConvidadoResponseDTO`: DTO de retorno contendo `id`, `eventoId`, `nome`, `email`, `telefone`, `acompanhantesPermitidos`, `observacoes` e `criadoEm`.
- `ConvidadoMapper`: Mapeamentos automáticos via MapStruct com proteção contra sobrescrita indevida de campos de auditoria e IDs.

### 2. Validações e Regras de Negócio (Strategy Pattern)
- `ConvidadoRepository`:
  - `existsByEventoIdAndEmailIgnoreCase`: Garante unicidade de e-mail por evento no cadastro (`RF-017`, `RN-012`).
  - `existsByEventoIdAndEmailIgnoreCaseAndIdNot`: Garante que, ao atualizar, o convidado não utilize e-mail já pertencente a outro convidado do mesmo evento.
  - `findAllByEventoIdAndBusca`: Consulta customizada em JPQL com `ILIKE` para busca textual por nome ou e-mail (`RF-019`).
  - `findByIdAndEventoId`: Consulta segura por ID vinculada estritamente ao evento.
- Validadores Strategy:
  - `ValidadorEmailDuplicadoCriacaoConvidado`: Lança `EmailConvidadoDuplicadoException` (HTTP 409 Conflict) em caso de duplicidade.
  - `ValidadorEmailDuplicadoAtualizacaoConvidado`: Lança `EmailConvidadoDuplicadoException` na edição se houver colisão de e-mail.
- Proteção de Eventos Inativos (`RN-014`): Impede criação, alteração ou exclusão de convidados quando o evento estiver `ENCERRADO` ou `CANCELADO`.
- Limite de Acompanhantes: Valida se a quantidade de acompanhantes permitidos informada não ultrapassa o limite padrão estipulado para o evento (`BusinessException` / HTTP 400).

### 3. Service e Controle de Acesso (RBAC & Ownership)
- `ConvidadoService`:
  - `criar`: Valida status do evento, executa validadores, valida teto de acompanhantes e persiste o convidado.
  - `listar`: Busca paginada com suporte a filtro `busca` (por nome ou e-mail).
  - `buscarPorId`, `atualizar`, `remover`: Validações completas de existência e permissão.
  - Isolamento de acesso: Usuários `ORGANIZADOR` só manipulam convidados de seus próprios eventos (`AccessDeniedException` / HTTP 403 para terceiros); usuários `ADMIN` possuem acesso irrestrito.

### 4. Controller e Rotas REST
- `ConvidadoController` mapeado em `/eventos/{eventoId}/convidados` (prefixo global `/api/v1/eventos/{eventoId}/convidados`):
  - `POST /api/v1/eventos/{eventoId}/convidados` -> HTTP 201 Created com header `Location`
  - `GET /api/v1/eventos/{eventoId}/convidados` -> HTTP 200 OK (Page com filtro opcional `busca`)
  - `GET /api/v1/eventos/{eventoId}/convidados/{id}` -> HTTP 200 OK
  - `PUT /api/v1/eventos/{eventoId}/convidados/{id}` -> HTTP 200 OK
  - `DELETE /api/v1/eventos/{eventoId}/convidados/{id}` -> HTTP 204 No Content
  - Todos os endpoints protegidos por `@PreAuthorize("hasAnyRole('ADMIN', 'ORGANIZADOR')")`.

### 5. Coleção HTTP (Postman)
- Arquivo `http/api-collection.json` atualizado com a pasta **Convidados**, incluindo variável `convidadoId`, script de captura automática do ID no teste de criação e payloads padronizados.

### 6. Testes de Integração (Testcontainers)
- `ConvidadoControllerTest`: 17 testes de integração executados contra PostgreSQL real via Testcontainers cobrindo 100% dos fluxos do Épico:
  1. Cadastro com dados válidos e header `Location` (HTTP 201).
  2. Bloqueio de e-mail duplicado no mesmo evento (HTTP 409).
  3. Permissão do mesmo e-mail em eventos distintos (HTTP 201).
  4. Validação de dados inválidos no DTO (HTTP 400).
  5. Bloqueio de requisições anônimas (HTTP 403).
  6. Bloqueio de organizador terceiro tentando acessar evento alheio (HTTP 403).
  7. Acesso e criação por usuário ADMIN em evento de terceiros (HTTP 201).
  8. Listagem de convidados paginada (HTTP 200).
  9. Busca textual com filtro por nome/e-mail (HTTP 200).
  10. Busca por ID existente (HTTP 200).
  11. Busca por ID inexistente (HTTP 404).
  12. Atualização bem-sucedida de convidado (HTTP 200).
  13. Bloqueio de atualização com e-mail duplicado de outro convidado (HTTP 409).
  14. Remoção de convidado com exclusão confirmada no banco (HTTP 204).
  15. Bloqueio de operações em evento cancelado/encerrado (HTTP 400).
  16. Bloqueio de criação com número de acompanhantes superior ao limite padrão do evento (HTTP 400).
  17. Bloqueio de atualização com número de acompanhantes superior ao limite padrão do evento (HTTP 400).
