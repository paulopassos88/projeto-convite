package br.com.passos.api_convite.infra.mail;

import br.com.passos.api_convite.domain.convite.dto.ConviteEmailPayloadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class EmailService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void enviarEmailConvite(ConviteEmailPayloadDTO payload) {
        String dataInicioFmt = payload.dataInicio() != null ? payload.dataInicio().format(FORMATTER) : "A definir";
        String dataTerminoFmt = payload.dataTermino() != null ? payload.dataTermino().format(FORMATTER) : "A definir";

        log.info("""
                ============================================================
                📧 SIMULAÇÃO DE DISPARO DE E-MAIL (MVP):
                ------------------------------------------------------------
                Para: {} <{}>
                Assunto: Seu convite para o evento: {}
                ------------------------------------------------------------
                Olá, {}!
                
                Você foi convidado para o evento '{}'.
                
                📅 Início:  {}
                📅 Término: {}
                📍 Local:   {}
                🗺️ Endereço: {}
                🔗 Localização: {}
                
                🔑 CÓDIGO DE ACESSO (PORTARIA): [{}]
                
                Instruções:
                Apresente este código de acesso na portaria do evento para realizar o check-in.
                Este código é pessoal e intransferível.
                ============================================================
                """,
                payload.convidadoNome(),
                payload.convidadoEmail(),
                payload.eventoNome(),
                payload.convidadoNome(),
                payload.eventoNome(),
                dataInicioFmt,
                dataTerminoFmt,
                payload.localNome(),
                payload.endereco(),
                payload.linkLocalizacao(),
                payload.codigo()
        );
    }
}
