package br.com.passos.api_convite.domain.convite.amqp;

import br.com.passos.api_convite.domain.convite.dto.ConviteEmailPayloadDTO;
import br.com.passos.api_convite.domain.convite.service.ConviteService;
import br.com.passos.api_convite.infra.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConviteEmailConsumer {

    private final EmailService emailService;
    private final ConviteService conviteService;

    @RabbitListener(queues = "${api.rabbitmq.queue:convite.email.enviar.queue}")
    public void processarEnvioConvite(ConviteEmailPayloadDTO payload) {
        log.info("Recebida mensagem da fila RabbitMQ para envio de e-mail: conviteId={}, email={}",
                payload.conviteId(), payload.convidadoEmail());

        try {
            emailService.enviarEmailConvite(payload);
            conviteService.registrarSucessoEnvio(payload.conviteId());
            log.info("E-mail de convite disparado e registrado com sucesso: conviteId={}", payload.conviteId());
        } catch (Exception ex) {
            log.error("Falha ao processar envio de e-mail do convite: conviteId={}, erro={}",
                    payload.conviteId(), ex.getMessage(), ex);
            conviteService.registrarFalhaEnvio(payload.conviteId());
        }
    }
}
