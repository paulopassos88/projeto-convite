package br.com.passos.api_convite.domain.convidado.repository;

import br.com.passos.api_convite.domain.convidado.model.Convidado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ConvidadoRepository extends JpaRepository<Convidado, UUID> {

    boolean existsByEventoIdAndEmailIgnoreCase(UUID eventoId, String email);

    boolean existsByEventoIdAndEmailIgnoreCaseAndIdNot(UUID eventoId, String email, UUID id);

    Page<Convidado> findAllByEventoId(UUID eventoId, Pageable pageable);

    @Query("""
        SELECT c FROM Convidado c
        WHERE c.evento.id = :eventoId
          AND (LOWER(c.nome) LIKE LOWER(CONCAT('%', :busca, '%'))
               OR LOWER(c.email) LIKE LOWER(CONCAT('%', :busca, '%')))
    """)
    Page<Convidado> findAllByEventoIdAndBusca(
            @Param("eventoId") UUID eventoId,
            @Param("busca") String busca,
            Pageable pageable);

    Optional<Convidado> findByIdAndEventoId(UUID id, UUID eventoId);
}
