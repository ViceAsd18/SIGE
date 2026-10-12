package cl.sige.plataforma.ms_calendario_reuniones.domain;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Proyeccion de solo lectura de una Evaluacion (fuente de verdad: ms-evaluaciones-notas). */
@Entity
@Table(name = "evaluacion_calendario")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EvaluacionCalendario {

    @Id
    @Column(name = "evaluacion_id")
    private Long id;

    @Column(name = "subperiodo_academico_id", nullable = false)
    private Long subperiodoAcademicoId;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    public EvaluacionCalendario(Long id, Long subperiodoAcademicoId, String nombre, LocalDate fecha) {
        this.id = id;
        this.subperiodoAcademicoId = subperiodoAcademicoId;
        this.nombre = nombre;
        this.fecha = fecha;
    }
}
