package br.com.passos.api_convite.domain.usuario.mapper;

import br.com.passos.api_convite.domain.usuario.dto.UsuarioResponseDTO;
import br.com.passos.api_convite.domain.usuario.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    UsuarioResponseDTO toResponse(Usuario usuario);
}
