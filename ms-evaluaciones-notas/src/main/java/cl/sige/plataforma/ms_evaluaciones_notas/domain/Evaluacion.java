package cl.sige.plataforma.ms_evaluaciones_notas.domain;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "evaluacion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Evaluacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "evaluacion_id")
    private Long id;

    //Referencia cruzada a ASIGNACION_DOCENTE en ms-academico.
    @Column(name = "asignacion_docente_id", nullable = false)
    private Long asignacionDocenteId;

    //Referencia cruzada a SUBPERIODO_ACADEMICO en ms-academico.
    @Column(name = "subperiodo_academico_id", nullable = false)
    private Long subperiodoAcademicoId;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "ponderacion", nullable = false)
    private Integer ponderacion;


    
}
