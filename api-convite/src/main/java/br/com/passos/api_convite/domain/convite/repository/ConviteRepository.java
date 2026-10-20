package br.com.passos.api_convite.domain.convite.repository;

import br.com.passos.api_convite.domain.convite.model.Convite;
import br.com.passos.api_convite.domain.convite.model.StatusConvite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConviteRepository extends JpaRepository<Convite, UUID> {

    boolean existsByCodigo(String codigo);

    Optional<Convite> findByCodigo(String codigo);

    Optional<Convite> findByConvidadoIdAndEventoId(UUID convidadoId, UUID eventoId);

    boolean existsByConvidadoIdAndEventoId(UUID convidadoId, UUID eventoId);

    Optional<Convite> findByIdAndEventoId(UUID id, UUID eventoId);

    Page<Convite> findAllByEventoId(UUID eventoId, Pageable pageable);

    Page<Convite> findAllByEventoIdAndStatus(UUID eventoId, StatusConvite status, Pageable pageable);
}
