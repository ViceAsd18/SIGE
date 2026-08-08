package cl.sige.plataforma.ms_academico.domain;

import java.time.LocalDate;

import cl.sige.plataforma.ms_academico.domain.enums.EstadoPeriodo;
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
@Table(name = "subperiodo_academico")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubperiodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subperiodo_academico_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "periodo_academico_id", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino", nullable = false)
    private LocalDate fechaTermino;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoPeriodo estado;

    public SubperiodoAcademico(PeriodoAcademico periodoAcademico, String nombre, LocalDate fechaInicio, LocalDate fechaTermino) {
        this.periodoAcademico = periodoAcademico;
        this.nombre = nombre;
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