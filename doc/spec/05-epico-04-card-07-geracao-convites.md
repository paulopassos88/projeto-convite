# 🎟️ Especificação Técnica: CARD-07 - Geração de Convites

**Status:** Concluído  
**Data de Conclusão:** 10/09/2026  

## 1. Visão Geral
Implementação do mecanismo de geração de convites com código único aleatório e seguro no escopo do MVP, vinculando os convidados aos respectivos eventos e inicializando o ciclo de vida do convite.

---

## 2. Requisitos & Regras de Negócio Atendidos

| Código | Descrição |
|:---|:---|
| **RF-021** | Geração de convite para cada convidado vinculado ao evento. |
| **RF-022** | Cada convite possui código único. |
| **RF-023** | Geração via `SecureRandom` com alfabeto legível (sem `0`, `O`, `1`, `I`, `l`) e alta entropia (~62 trilhões de combinações). |
| **RF-024 / RF-025** | Identificador seguro para representação e conferência. |
| **RF-030** | Estado inicial do convite como `PENDENTE` e status de envio como `NAO_ENVIADO`. |
| **RN-001** | Unicidade garantida por índice `UNIQUE` e validação de colisão com retry. |
| **RN-015** | Segurança do código (não sequencial, não previsível, validado no servidor). |

---

## 3. Endpoints Implementados

| Método | Rota | Descrição | Permissão | Status |
|:---|:---|:---|:---|:---|
| `POST` | `/api/v1/eventos/{eventoId}/convidados/{convidadoId}/convite` | Gera o convite com código único | `ADMIN`, `ORGANIZADOR` | `201 Created` |
| `GET` | `/api/v1/eventos/{eventoId}/convites` | Lista convites do evento (paginado, filtro opcional por `status`) | `ADMIN`, `ORGANIZADOR` | `200 OK` |
| `GET` | `/api/v1/eventos/{eventoId}/convites/{id}` | Busca convite por ID | `ADMIN`, `ORGANIZADOR` | `200 OK` |
| `GET` | `/api/v1/eventos/{eventoId}/convidados/{convidadoId}/convite` | Busca convite pelo ID do convidado | `ADMIN`, `ORGANIZADOR` | `200 OK` |

---

## 4. Componentes Criados

1. **`GeradorCodigoConviteService`:** Gerador de códigos aleatórios seguros de 8 caracteres utilizando `SecureRandom` e checagem de colisão no banco.
2. **`ConviteRepository`:** Interface Spring Data JPA com métodos de busca e existência por evento, convidado e código.
3. **`ConviteResponseDTO`:** Record para resposta limpa da API contendo dados do convidado, evento, código, status e envio.
4. **`ConviteMapper`:** Conversor MapStruct da entidade `Convite` para `ConviteResponseDTO`.
5. **`ConviteService`:** Serviço de aplicação com validações de segurança RBAC/Ownership (dono do evento ou `ADMIN`), integridade do evento e regras de negócio.
6. **`ConviteController`:** Controller REST expondo as operações do recurso.
7. **Exceções Especializadas:** `ConviteNaoEncontradoException` (`404 Not Found`) e `ConviteJaGeradoException` (`409 Conflict`).

---

## 5. Testes de Integração (Testcontainers)

Cobertura implementada em `ConviteControllerTest.java` (cenários 1 a 13):
- **Cenário 1:** Geração de convite com sucesso (`201 Created`), header `Location`, código gerado, `status = PENDENTE` e `statusEnvio = NAO_ENVIADO`.
- **Cenário 2:** Bloqueio de geração duplicada para o mesmo convidado (`409 Conflict`).
- **Cenário 3:** Retorno `404 Not Found` para convidado inexistente.
- **Cenário 4:** Retorno `404 Not Found` quando o convidado pertence a outro evento.
- **Cenário 5:** Bloqueio de geração de convite em evento cancelado (`400 Bad Request`).
- **Cenário 6:** Bloqueio de requisições não autenticadas (`403 Forbidden`).
- **Cenário 7:** Bloqueio quando um organizador tenta gerar convite em evento de outro organizador (`403 Forbidden`).
- **Cenário 8:** Permissão para usuário `ADMIN` gerar convite em qualquer evento (`201 Created`).
- **Cenário 9:** Listagem paginada de convites de um evento (`200 OK`).
- **Cenário 10:** Listagem de convites com filtro por `status` (`200 OK`).
- **Cenário 11:** Busca de convite por ID (`200 OK`).
- **Cenário 12:** Busca de convite pelo ID do convidado (`200 OK`).
- **Cenário 13:** Retorno `404 Not Found` ao buscar convite por ID inexistente.

