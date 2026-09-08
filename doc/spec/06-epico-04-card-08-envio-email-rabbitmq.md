# 📬 Especificação Técnica: CARD-08 - Envio de E-mail Assíncrono (RabbitMQ)

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

## 3. Arquitetura de Mensageria (RabbitMQ)

### 3.1 Topologia AMQP Proposta
- **Exchange:** `convite.exchange` (Tipo: `Direct` ou `Topic`)
- **Routing Key:** `convite.email.enviar`
- **Queue Principal:** `convite.email.enviar.queue` (Durable)
- **Dead Letter Exchange (DLX):** `convite.dlx` (para mensagens que excederem tentativas em caso de erro)
- **Dead Letter Queue (DLQ):** `convite.email.enviar.dlq`

### 3.2 Payload da Mensagem (`ConviteEmailPayloadDTO`)
```json
{
  "conviteId": "UUID",
  "codigo": "7kM9xP2a",
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
1. O Organizador aciona o endpoint `POST /convites/{id}/enviar` (ou envio em lote).
2. A API valida o convite, monta o payload e publica a mensagem na exchange do RabbitMQ.
3. O status do convite é atualizado (ou mantido em processamento) e a API responde imediatamente com `202 Accepted` ou `200 OK`.
4. O Worker/Consumer consome a mensagem da fila:
   - Se sucesso: Dispara o e-mail via serviço de SMTP (ou mock no MVP), atualiza `status_envio = ENVIADO` e `enviado_em = now()`.
   - Se erro transitório/falha: Tenta retentativas (retry) ou joga na DLQ e registra `status_envio = FALHA`.

---

## 4. Endpoints Envolvidos

| Método | Rota | Descrição | Permissão | Status Retorno |
|:---|:---|:---|:---|:---|
| `POST` | `/api/v1/convites/{id}/enviar` | Envia convite por e-mail de forma assíncrona | `ORGANIZADOR`, `ADMIN` | `200 OK` / `202 Accepted` |
| `GET` | `/api/v1/eventos/{eventoId}/convites` | Lista convites exibindo `statusEnvio` e `enviadoEm` | `ORGANIZADOR`, `ADMIN` | `200 OK` |
