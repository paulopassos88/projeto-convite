# 🎟️ Especificação Técnica: CARD-07 - Geração de Convites

## 1. Visão Geral
Implementação do mecanismo de geração de convites com código único aleatório e seguro no escopo do MVP, vinculando os convidados aos respectivos eventos e inicializando o ciclo de vida do convite.

---

## 2. Requisitos & Regras de Negócio Atendidos

| Código | Descrição |
|:---|:---|
| **RF-021** | Geração de convite para cada convidado vinculado ao evento. |
| **RF-022** | Cada convite possui código único. |
| **RF-023** | Geração via `SecureRandom` com alfabeto legível (sem 0, O, 1, I, l) e alta entropia (~62 trilhões de combinações). |
| **RF-024 / RF-025** | Identificador seguro para representação e conferência. |
| **RF-030** | Estado inicial do convite como `PENDENTE` e status de envio como `NAO_ENVIADO`. |
| **RN-001** | Unicidade garantida por índice `UNIQUE` e validação de colisão com retry. |
| **RN-015** | Segurança do código (não sequencial, não previsível, validado no servidor). |

---

## 3. Endpoints Implementados

| Método | Rota | Descrição | Permissão | Status |
|:---|:---|:---|:---|:---|
| `POST` | `/api/v1/eventos/{eventoId}/convidados/{convidadoId}/convite` | Gera o convite com código único | `ADMIN`, `ORGANIZADOR` | 201 Created |
| `GET` | `/api/v1/eventos/{eventoId}/convites` | Lista convites do evento (paginado, filtro por status) | `ADMIN`, `ORGANIZADOR` | 200 OK |
| `GET` | `/api/v1/eventos/{eventoId}/convites/{id}` | Busca convite por ID | `ADMIN`, `ORGANIZADOR` | 200 OK |
| `GET` | `/api/v1/eventos/{eventoId}/convidados/{convidadoId}/convite` | Busca convite pelo ID do convidado | `ADMIN`, `ORGANIZADOR` | 200 OK |

---

## 4. Componentes Criados

1. **`GeradorCodigoConviteService`:** Gerador de códigos aleatórios seguros de 8 dígitos utilizando `SecureRandom` e checagem de colisão no banco.
2. **`ConviteRepository`:** Interface Spring Data JPA com métodos de busca e existência por evento/convidado/código.
3. **`ConviteResponseDTO`:** Record para resposta limpa da API contendo dados do convidado, evento, código e status.
4. **`ConviteMapper`:** Conversor da entidade `Convite` para `ConviteResponseDTO`.
5. **`ConviteService`:** Serviço de aplicação com validações de segurança RBAC (dono do evento ou ADMIN), integridade do evento e regras de negócio.
6. **`ConviteController`:** Controller REST expondo as operações do recurso.
7. **Exceções Especializadas:** `ConviteNaoEncontradoException` (404) e `ConviteJaGeradoException` (409).
