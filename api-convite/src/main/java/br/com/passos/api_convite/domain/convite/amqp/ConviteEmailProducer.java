package br.com.passos.api_convite.domain.convite.amqp;

import br.com.passos.api_convite.domain.convite.dto.ConviteEmailPayloadDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConviteEmailProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${api.rabbitmq.exchange:convite.exchange}")
    private String exchange;

    @Value("${api.rabbitmq.routing-key:convite.email.enviar}")
    private String routingKey;

    public void enviarConviteEmail(ConviteEmailPayloadDTO payload) {
        log.info("Publicando convite no RabbitMQ para envio assíncrono: conviteId={}, email={}",
                payload.conviteId(), payload.convidadoEmail());
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }
}
