# 📬 Especificação Técnica: CARD-08 - Envio de E-mail Assíncrono (RabbitMQ)

**Status:** Concluído  
**Data de Conclusão:** 16/09/2026  

## 1. Visão Geral
Implementação do fluxo assíncrono de envio de e-mails para os convidados de um evento utilizando RabbitMQ como Message Broker (mensageria AMQP). Esse padrão desacopla a requisição HTTP da API do processo de montagem e disparo de e-mails, garantindo alta disponibilidade, tolerância a falhas e resposta imediata ao usuário.

---

## 2. Requisitos Funcionais & Regras de Negócio Atendidos

| Código | Descrição |
|:---|:---|
| **RF-031** | O sistema deve enviar convite por e-mail ao convidado de forma assíncrona. |
| **RF-032** | O e-mail deve conter o nome do convidado. |
| **RF-033** | O e-mail deve conter o nome do evento. |
| **RF-034** | O e-mail deve conter a data e horário de início/término do evento. |
| **RF-035** | O e-mail deve conter o local e endereço do evento. |
| **RF-036** | O e-mail deve conter link para Google Maps e/ou Waze (baseado no endereço/local). |
| **RF-037** | O e-mail deve conter o código de acesso legível (representação do QR Code no MVP). |
| **RF-039** | O e-mail deve conter instruções claras de apresentação na portaria. |
| **RF-040** | O sistema deve registrar a data/hora exata do envio (`enviado_em`). |
| **RF-041** | O sistema deve registrar falhas de envio (`status_envio = FALHA`). |
| **RF-042** | O organizador pode consultar o status de envio de cada convite (`NAO_ENVIADO`, `ENVIADO`, `FALHA`). |
| **RN-013** | E-mail do convidado válido; disparo desacoplado; permitir reenvio em caso de falha; não criar credenciais de usuário. |

---

## 3. Arquitetura de Mensageria Implementada (RabbitMQ)

### 3.1 Topologia AMQP (`RabbitMQConfig`)
- **Exchange:** `convite.exchange` (`DirectExchange`, durable)
- **Routing Key:** `convite.email.enviar`
- **Queue Principal:** `convite.email.enviar.queue` (durable)
- **Serialização:** `JacksonJsonMessageConverter` integrado ao `RabbitTemplate`

### 3.2 Payload da Mensagem (`ConviteEmailPayloadDTO`)
```json
{
  "conviteId": "880e8400-e29b-41d4-a716-446655440003",
  "codigo": "7KM9XP2A",
  "convidadoNome": "Carlos Silva",
  "convidadoEmail": "carlos@teste.com",
  "eventoNome": "Casamento Real",
  "eventoDescricao": "Cerimônia e festa",
  "localNome": "Villa Bisutti",
  "endereco": "Rua das Flores, 123 - SP",
  "dataInicio": "2026-10-10T19:00:00",
  "dataTermino": "2026-10-11T02:00:00",
  "linkLocalizacao": "https://www.google.com/maps/search/?api=1&query=Villa+Bisutti"
}
```

### 3.3 Ciclo de Vida do Envio
1. O Organizador (dono do evento) ou `ADMIN` aciona o endpoint `POST /api/v1/convites/{id}/enviar`.
2. O `ConviteService` valida a existência do convite, a propriedade do evento, se o evento está `ATIVO` e se o convite não está `REVOGADO`.
3. O `ConviteEmailProducer` publica o `ConviteEmailPayloadDTO` na exchange `convite.exchange` e a API responde imediatamente com `200 OK` contendo `MensagemSucessoDTO` (`conviteId`, `mensagem`, `enfileiradoEm`).
4. O `ConviteEmailConsumer` (`@RabbitListener`) consome a mensagem da fila `convite.email.enviar.queue`:
   - **Sucesso:** Dispara o e-mail formatado via `EmailService` e chama `conviteService.registrarSucessoEnvio(conviteId)`, atualizando `status_envio = ENVIADO` e `enviado_em = LocalDateTime.now()`.
   - **Falha:** Captura a exceção, registra log de erro e chama `conviteService.registrarFalhaEnvio(conviteId)`, atualizando `status_envio = FALHA`.

---

## 4. Componentes Criados

1. **`RabbitMQConfig` (`infra/amqp`):** Configuração dos beans de `DirectExchange`, `Queue`, `Binding`, `JacksonJsonMessageConverter` e `RabbitTemplate`.
2. **`EmailService` (`infra/mail`):** Serviço responsável pela formatação e disparo do e-mail contendo todos os dados do evento, links de localização, código de acesso e instruções da portaria.
3. **`ConviteEmailPayloadDTO` e `MensagemSucessoDTO` (`domain/convite/dto`):** Records para transporte da mensagem AMQP e resposta do enfileiramento HTTP.
4. **`ConviteEmailProducer` (`domain/convite/amqp`):** Publicador AMQP das solicitações de envio.
5. **`ConviteEmailConsumer` (`domain/convite/amqp`):** Worker consumidor da fila que orquestra o disparo e atualiza o status de envio no banco.

---

## 5. Endpoints Envolvidos

| Método | Rota | Descrição | Permissão | Status Retorno |
|:---|:---|:---|:---|:---|
| `POST` | `/api/v1/convites/{id}/enviar` | Enfileira o envio de convite por e-mail no RabbitMQ | `ADMIN`, `ORGANIZADOR` | `200 OK` |
| `GET` | `/api/v1/eventos/{eventoId}/convites` | Lista convites exibindo `statusEnvio` e `enviadoEm` | `ADMIN`, `ORGANIZADOR` | `200 OK` |

---

## 6. Testes Automatizados

- **`ConviteControllerTest` (Cenários 14 a 17):**
  - **Cenário 14:** Enfileiramento de envio com sucesso (`200 OK`) e verificação da publicação no `ConviteEmailProducer`.
  - **Cenário 15:** Retorno `404 Not Found` ao tentar enviar convite inexistente.
  - **Cenário 16:** Bloqueio (`403 Forbidden`) quando outro organizador tenta enviar convite de evento alheio.
  - **Cenário 17:** Bloqueio (`400 Bad Request`) ao tentar enviar convite com status `REVOGADO`.
- **`ConviteEmailConsumerTest`:** Valida o processamento da mensagem pelo consumidor, atualização para `ENVIADO` com `enviadoEm` preenchido em caso de sucesso, e marcação como `FALHA` quando ocorre erro no envio.

