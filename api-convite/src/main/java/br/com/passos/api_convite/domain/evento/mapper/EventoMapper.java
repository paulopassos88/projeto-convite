package br.com.passos.api_convite.domain.evento.mapper;

import br.com.passos.api_convite.domain.evento.dto.AtualizarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.CriarEventoDTO;
import br.com.passos.api_convite.domain.evento.dto.EventoResponseDTO;
import br.com.passos.api_convite.domain.evento.model.Evento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EventoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizador", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Evento toEntity(CriarEventoDTO dto);

    @Mapping(source = "organizador.nome", target = "organizadorNome")
    EventoResponseDTO toResponseDTO(Evento evento);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "organizador", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    void updateEntityFromDTO(AtualizarEventoDTO dto, @MappingTarget Evento evento);
}
