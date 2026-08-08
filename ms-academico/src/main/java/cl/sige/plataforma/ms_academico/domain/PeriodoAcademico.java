package cl.sige.plataforma.ms_academico.domain;

import java.time.LocalDate;

import cl.sige.plataforma.ms_academico.domain.enums.EstadoPeriodo;
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
@Table(name = "periodo_academico")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "periodo_academico_id")
    private Long id;

    @Column(name = "nombre_anio", nullable = false, unique = true, length = 20)
    private String nombreAnio;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino", nullable = false)
    private LocalDate fechaTermino;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPeriodo estado;

    public PeriodoAcademico(String nombreAnio, LocalDate fechaInicio, LocalDate fechaTermino) {
        this.nombreAnio = nombreAnio;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.estado = EstadoPeriodo.ABIERTO;
    }

    public void cerrar() {
        this.estado = EstadoPeriodo.CERRADO;
    }

    public void reabrir() {
        this.estado = EstadoPeriodo.ABIERTO;
    }


}
