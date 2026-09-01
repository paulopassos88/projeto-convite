package br.com.passos.api_convite.domain.checkin.model;

import br.com.passos.api_convite.domain.convite.model.Convite;
import br.com.passos.api_convite.domain.evento.model.Evento;
import br.com.passos.api_convite.domain.usuario.model.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "checkin")
@Getter
@Setter
@NoArgsConstructor
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "convite_id", nullable = false)
    private Convite convite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "controlador_id", nullable = false)
    private Usuario controlador;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_entrada", nullable = false)
    private TipoEntrada tipoEntrada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultadoCheckIn resultado;

    @Column(name = "motivo_negativa")
    private String motivoNegativa;

    private String dispositivo;

    @PrePersist
    public void prePersist() {
        this.dataHora = LocalDateTime.now();
    }
}
