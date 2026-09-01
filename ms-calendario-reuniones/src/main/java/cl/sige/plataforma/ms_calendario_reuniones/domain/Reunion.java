package cl.sige.plataforma.ms_calendario_reuniones.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.EstadoReunion;
import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoReunion;
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
@Table(name = "reunion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reunion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reunion_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoReunion tipo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_termino", nullable = false)
    private LocalTime horaTermino;

    //Referencia cruzada a PERSONA_ROL en ms-identidad-acceso
    @Column(name = "convocante_persona_rol_id", nullable = false)
    private Long convocantePersonaRolId;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoReunion estado;

    @Column(name = "lugar_modalidad", length = 200)
    private String lugarModalidad;

    public Reunion (TipoReunion tipo, LocalDate fecha, LocalTime horaInicio, LocalTime horaTermino, Long convocantePersonaRolId, String lugarModalidad) {
        this.tipo = tipo;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaTermino = horaTermino;
        this.convocantePersonaRolId = convocantePersonaRolId;
        this.lugarModalidad = lugarModalidad;
        this.estado = EstadoReunion.PROGRAMADA;
    }

    public void marcarRealizada() {
        this.estado = EstadoReunion.REALIZADA;
    }

    public void cancelar() {
        this.estado = EstadoReunion.CANCELADA;
    }


}
