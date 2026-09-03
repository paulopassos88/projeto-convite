package br.com.passos.api_convite.domain.convidado.mapper;

import br.com.passos.api_convite.domain.convidado.dto.AtualizarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.dto.ConvidadoResponseDTO;
import br.com.passos.api_convite.domain.convidado.dto.CriarConvidadoDTO;
import br.com.passos.api_convite.domain.convidado.model.Convidado;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ConvidadoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "evento", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    Convidado toEntity(CriarConvidadoDTO dto);

    @Mapping(source = "evento.id", target = "eventoId")
    ConvidadoResponseDTO toResponseDTO(Convidado convidado);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "evento", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    void updateEntityFromDTO(AtualizarConvidadoDTO dto, @MappingTarget Convidado convidado);
}
