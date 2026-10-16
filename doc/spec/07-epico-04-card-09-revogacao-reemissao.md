# 🚫 Especificação Técnica: CARD-09 - Revogação e Reemissão de Convites

## 1. Visão Geral
Implementação do fluxo de revogação de convites e suporte à reemissão de convite revogado no escopo do MVP, garantindo segurança de acesso, controle de permissões por perfil (RBAC) e propriedade (Ownership).

---

## 2. Requisitos & Regras de Negócio Atendidos

| Código | Descrição |
|:---|:---|
| **RF-026** | O sistema permite reenvio individual de convite. |
| **RF-028** | O sistema permite revogar um convite. |
| **RF-029** | O sistema permite gerar novo código para um convite revogado ou comprometido. |
| **RF-030** | O convite possui status (`REVOGADO`, `PENDENTE`, etc.). |
| **RN-010** | O organizador pode revogar um convite a qualquer momento antes do uso. Após revogado, o convite não pode mais ser validado e o status é alterado para `REVOGADO`. |
| **RN-011** | Se um convite for revogado, o organizador pode gerar novo código. O código anterior é invalidado e o novo código mantém vínculo com o mesmo convidado/evento. |
| **RN-014** | Bloqueio de operações em eventos encerrados ou cancelados. |

---

## 3. Endpoints Envolvidos

| Método | Rota | Descrição | Permissão | Status Retorno |
|:---|:---|:---|:---|:---|
| `PATCH` | `/api/v1/convites/{id}/revogar` | Revoga o convite existente | `ADMIN`, `ORGANIZADOR` | `204 No Content` |
| `POST` | `/api/v1/eventos/{eventoId}/convidados/{convidadoId}/convite` | Gera novo convite ou reemite caso esteja revogado | `ADMIN`, `ORGANIZADOR` | `201 Created` |

---

## 4. Regras de Validação Implementadas

1. **Validação de Existência:** Convite ou convidado inexistente retorna `404 Not Found`.
2. **Validação de Permissão (Ownership / RBAC):** Apenas o organizador dono do evento ou usuários `ADMIN` podem revogar ou reemitir convites (`403 Forbidden`).
3. **Validação de Evento Ativo:** Operações em eventos cancelados ou encerrados são bloqueadas com `400 Bad Request`.
4. **Validação de Status do Convite:**
   - Convite com status `UTILIZADO` não pode ser revogado (`400 Bad Request`).
   - Convite já com status `REVOGADO` não pode ser revogado novamente (`400 Bad Request`).
5. **Reemissão Automática (RN-011):**
   - Ao solicitar geração de convite para um convidado cujo convite anterior foi `REVOGADO`, o sistema gera um novo código criptograficamente seguro (`SecureRandom`), reseta o status para `PENDENTE`, `statusEnvio` para `NAO_ENVIADO` e limpa `enviadoEm`.

---

## 5. Testes de Integração (Testcontainers)

Cobertura completa adicionada em `ConviteControllerTest.java` (cenários 18 a 25):
- **Cenário 18:** Revogação com sucesso e retorno `204 No Content`.
- **Cenário 19:** Bloqueio de revogação de convite já utilizado (`400 Bad Request`).
- **Cenário 20:** Bloqueio de revogação de convite já revogado (`400 Bad Request`).
- **Cenário 21:** Bloqueio de organizador terceiro tentando revogar convite alheio (`403 Forbidden`).
- **Cenário 22:** Retorno `404 Not Found` para convite inexistente.
- **Cenário 23:** Bloqueio de revogação em evento cancelado (`400 Bad Request`).
- **Cenário 24:** Reemissão de convite revogado com novo código único e status `PENDENTE` (`201 Created`).
- **Cenário 25:** Permissão para usuário `ADMIN` revogar convite de qualquer evento (`204 No Content`).
