package cl.sige.plataforma.ms_academico.domain;

import java.time.LocalDate;

import cl.sige.plataforma.ms_academico.domain.enums.EstadoMatricula;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.GeneratedValue;

@Entity
@Table(name = "matricula")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "matricula_id")
    private Long id;

    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoMatricula estado;

    @Column(name = "motivo", length = 255)
    private String motivo;

    @Column(name = "fecha_inicio_vigencia", nullable = false)
    private LocalDate fechaInicioVigencia;

    @Column(name = "fecha_termino_vigencia")
    private LocalDate fechaTerminoVigencia;

    public Matricula(Long estudianteId, Curso curso, LocalDate fechaInicioVigencia) {
        this.estudianteId = estudianteId;
        this.curso = curso;
        this.fechaInicioVigencia = fechaInicioVigencia;
        this.estado = EstadoMatricula.ACTIVA;
    }

    public void finalizar(String motivo) {
        this.estado = EstadoMatricula.FINALIZADA;
        this.motivo = motivo;
        this.fechaTerminoVigencia = LocalDate.now();
    }

    public void retirar(String motivo) {
        this.estado = EstadoMatricula.RETIRADA;
        this.motivo = motivo;
        this.fechaTerminoVigencia = LocalDate.now();
    }
}