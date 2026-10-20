package br.com.passos.api_convite.domain.convite.amqp;

import br.com.passos.api_convite.domain.convite.dto.ConviteEmailPayloadDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ConviteEmailProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private ConviteEmailProducer producer;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(producer, "exchange", "convite.exchange");
        ReflectionTestUtils.setField(producer, "routingKey", "convite.email.enviar");
    }

    @Test
    @DisplayName("Deve publicar mensagem no RabbitMQ com a exchange e routing key corretas")
    void enviarConviteEmail_PublicaMensagemNoRabbitMQ() {
        ConviteEmailPayloadDTO payload = new ConviteEmailPayloadDTO(
                UUID.randomUUID(),
                "COD12345",
                "Carlos Silva",
                "carlos@teste.com",
                "Festa de Gala",
                "Descrição",
                "Salão Nobre",
                "Rua Principal, 100",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4),
                "https://maps.google.com"
        );

        producer.enviarConviteEmail(payload);

        verify(rabbitTemplate).convertAndSend("convite.exchange", "convite.email.enviar", payload);
    }
}
