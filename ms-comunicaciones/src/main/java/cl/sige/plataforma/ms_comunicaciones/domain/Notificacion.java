package cl.sige.plataforma.ms_comunicaciones.domain;

import java.time.LocalDateTime;

import cl.sige.plataforma.ms_comunicaciones.domain.enums.EstadoNotificacion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "notificacion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notificacion_id")
    private Long id;

    //Referencia cruzada a PERSONA en ms-identidad-acceso
    @Column(name = "destinatario_persona_id", nullable = false)
    private Long destinatarioPersonaId;

    @Column(name = "tipo_evento", nullable = false, length = 50)
    private String tipoEvento;

    @Column(name = "entidad_origen_tipo", length = 50)
    private String entidadOrigenTipo;

    @Column(name = "entidad_origen_id")
    private Long entidadOrigenId;

    @Column(name = "contenido", nullable = false, length = 1000)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoNotificacion estado;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_lectura")
    private LocalDateTime fechaLectura;

    public Notificacion (Long destinatarioPersonaRolId, String tipoEvento, String entidadOrigenTipo, Long entidadOrigenId, String contenido) {
        this.destinatarioPersonaId = destinatarioPersonaRolId;
        this.tipoEvento = tipoEvento;
        this.entidadOrigenTipo = entidadOrigenTipo;
        this.entidadOrigenId = entidadOrigenId;
        this.contenido = contenido;
        this.estado = EstadoNotificacion.ENVIADA;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void marcaLeida() {
        this.estado = EstadoNotificacion.LEIDA;
        this.fechaLectura = LocalDateTime.now();
    }


}
