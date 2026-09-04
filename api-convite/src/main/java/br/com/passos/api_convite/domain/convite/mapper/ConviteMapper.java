package br.com.passos.api_convite.domain.convite.mapper;

import br.com.passos.api_convite.domain.convite.dto.ConviteResponseDTO;
import br.com.passos.api_convite.domain.convite.model.Convite;
import org.springframework.stereotype.Component;

@Component
public class ConviteMapper {

    public ConviteResponseDTO toResponseDTO(Convite convite) {
        if (convite == null) {
            return null;
        }

        return new ConviteResponseDTO(
                convite.getId(),
                convite.getConvidado() != null ? convite.getConvidado().getId() : null,
                convite.getConvidado() != null ? convite.getConvidado().getNome() : null,
                convite.getConvidado() != null ? convite.getConvidado().getEmail() : null,
                convite.getEvento() != null ? convite.getEvento().getId() : null,
                convite.getEvento() != null ? convite.getEvento().getNome() : null,
                convite.getCodigo(),
                convite.getQrCodeUrl(),
                convite.getStatus(),
                convite.getStatusEnvio(),
                convite.getEnviadoEm(),
                convite.getCriadoEm()
        );
    }
}
