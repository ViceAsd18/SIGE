package cl.sige.plataforma.ms_academico.domain;

import java.time.LocalDate;

import cl.sige.plataforma.ms_academico.domain.enums.EstadoVigencia;
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
@Table(name = "asignacion_docente")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsignacionDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asignacion_docente_id")
    private Long id;

    @Column(name = "docente_persona_rol_id", nullable = false)
    private Long docentePersonaRolId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "fecha_inicio_vigencia", nullable = false)
    private LocalDate fechaInicioVigencia;

    @Column(name = "fecha_termino_vigencia")
    private LocalDate fechaTerminoVigencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoVigencia estado;

    public AsignacionDocente(Long docentePersonaRolId, Asignatura asignatura, Curso curso, LocalDate fechaInicioVigencia) {
        this.docentePersonaRolId = docentePersonaRolId;
        this.asignatura = asignatura;
        this.curso = curso;
        this.fechaInicioVigencia = fechaInicioVigencia;
        this.estado = EstadoVigencia.VIGENTE;
    }

    public void finalizarVigencia(LocalDate fechaTermino) {
        this.estado = EstadoVigencia.NO_VIGENTE;
        this.fechaTerminoVigencia = fechaTermino;
    }
    
}