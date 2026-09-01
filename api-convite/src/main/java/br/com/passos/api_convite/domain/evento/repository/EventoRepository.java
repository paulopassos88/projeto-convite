package br.com.passos.api_convite.domain.evento.repository;

import br.com.passos.api_convite.domain.evento.model.Evento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {

    Page<Evento> findAllByOrganizadorId(UUID organizadorId, Pageable pageable);

    boolean existsByIdAndOrganizadorId(UUID id, UUID organizadorId);
}
