package cl.sige.plataforma.ms_evaluaciones_notas.domain;

import java.math.BigDecimal;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "calificacion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Calificacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calificacion_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    //Referencia cruzada a ESTUDIANTE en ms-estudiantes
    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    @Column(name = "resultado", nullable = false, precision = 5, scale = 1)
    private BigDecimal resultado;

    public Calificacion(Evaluacion evaluacion, Long estudianteId, BigDecimal resultado) {
        this.evaluacion = evaluacion;
        this.estudianteId = estudianteId;
        this.resultado = resultado;
    }

    public void modificar(BigDecimal nuevoResultado) {
        this.resultado = nuevoResultado;
    }

}
