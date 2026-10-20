package br.com.passos.api_convite.domain.convite.amqp;

import br.com.passos.api_convite.domain.convite.dto.ConviteEmailPayloadDTO;
import br.com.passos.api_convite.domain.convite.service.ConviteService;
import br.com.passos.api_convite.infra.mail.EmailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConviteEmailConsumerTest {

    @Mock
    private EmailService emailService;

    @Mock
    private ConviteService conviteService;

    @InjectMocks
    private ConviteEmailConsumer consumer;

    @Test
    @DisplayName("Deve disparar e-mail e registrar sucesso no banco ao consumir mensagem válida")
    void processarEnvioConvite_ComSucesso_RegistraSucesso() {
        UUID conviteId = UUID.randomUUID();
        ConviteEmailPayloadDTO payload = new ConviteEmailPayloadDTO(
                conviteId,
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

        consumer.processarEnvioConvite(payload);

        verify(emailService).enviarEmailConvite(payload);
        verify(conviteService).registrarSucessoEnvio(conviteId);
        verify(conviteService, never()).registrarFalhaEnvio(any());
    }

    @Test
    @DisplayName("Deve registrar falha de envio no banco quando ocorrer exceção no envio do e-mail")
    void processarEnvioConvite_ComErro_RegistraFalha() {
        UUID conviteId = UUID.randomUUID();
        ConviteEmailPayloadDTO payload = new ConviteEmailPayloadDTO(
                conviteId,
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

        doThrow(new RuntimeException("Falha SMTP")).when(emailService).enviarEmailConvite(payload);

        consumer.processarEnvioConvite(payload);

        verify(emailService).enviarEmailConvite(payload);
        verify(conviteService).registrarFalhaEnvio(conviteId);
        verify(conviteService, never()).registrarSucessoEnvio(any());
    }
}
