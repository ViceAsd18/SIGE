package cl.sige.plataforma.ms_estudiantes.domain;

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
@Table(name = "apoderado_estudiante")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApoderadoEstudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apoderado_estudiante_id")
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "apoderado_id", nullable = false)
    private Apoderado apoderado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_relacion", nullable = false, length = 20)
    private TipoRelacionApoderado tipoRelacion;


    public ApoderadoEstudiante(Apoderado apoderado, Estudiante estudiante, TipoRelacionApoderado tipoRelacion) {
        this.apoderado = apoderado;
        this.estudiante = estudiante;
        this.tipoRelacion = tipoRelacion;
    }

}
