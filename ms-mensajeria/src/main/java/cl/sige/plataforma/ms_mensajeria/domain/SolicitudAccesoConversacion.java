package cl.sige.plataforma.ms_mensajeria.domain;

import java.time.LocalDateTime;

import cl.sige.plataforma.ms_mensajeria.domain.enums.EstadoSolicitud;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitud_acceso_conversacion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolicitudAccesoConversacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_acceso_conversacion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversacion_id", nullable = false)
    private Conversacion conversacion;

    //Referencia Cruzada a PERSONA_ROL en ms-identidad-acceso
    @Column(name = "solicitante_persona_rol_id", nullable = false)
    private Long solicitantePersonaRolId;

    @Column(name = "aprobador_persona_rol_id")
    private Long aprobadorPersonaRolId;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitud estado;

    @Column(name = "fecha_solicitud", nullable = false, updatable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    public SolicitudAccesoConversacion(Conversacion conversacion, Long solicitantePersonaRolId, String motivo) {
        this.conversacion = conversacion;
        this.solicitantePersonaRolId = solicitantePersonaRolId;
        this.motivo = motivo;
        this.estado = EstadoSolicitud.PENDIENTE;
        this.fechaSolicitud = LocalDateTime.now();
    }

    public void aprobar(Long aprobadorPersonaRolId) {
        this.estado = EstadoSolicitud.APROBADA;
        this.aprobadorPersonaRolId = aprobadorPersonaRolId;
        this.fechaResolucion = LocalDateTime.now();
    }

    public void rechazar(Long aprobadorPersonaRolId) {
        this.estado = EstadoSolicitud.RECHAZADA;
        this.aprobadorPersonaRolId = aprobadorPersonaRolId;
        this.fechaResolucion = LocalDateTime.now();
    }

    public void marcarEjecutada() {
        this.estado = EstadoSolicitud.EJECUTADA;
    }

}
