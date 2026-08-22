package cl.sige.plataforma.ms_evaluaciones_notas.domain;

import java.time.LocalDateTime;


import cl.sige.plataforma.ms_evaluaciones_notas.domain.enums.EstadoSolicitud;
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
@Table(name = "solicitud_excepcion_calificacion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SolicitudExcepcionCalificacion {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "solicitud_excepcion_calificacion_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "calificacion_id", nullable = false)
    private Calificacion calificacion;

    //Referencia cruzada a PERSONA_ROL en ms-identidad-acceso
    @Column(name = "solicitante_persona_rol_id", nullable = false)
    private Long solicitudPersonaRolId;

    @Column(name = "aprobador_persona_rol_id", nullable = false)
    private Long aprobadorPersonaRolId;

    @Column(name = "motivo", nullable = false, length = 500)
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoSolicitud estado;

    @Column(name = "fecha_solicitud", nullable = false)
    private LocalDateTime fechaSolicitud;

    @Column(name = "fecha_resolucion")
    private LocalDateTime fechaResolucion;

    public SolicitudExcepcionCalificacion(Calificacion calificacion, Long solicitudPersonaRolId, String motivo) {
        this.calificacion = calificacion;
        this.solicitudPersonaRolId = solicitudPersonaRolId;
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
