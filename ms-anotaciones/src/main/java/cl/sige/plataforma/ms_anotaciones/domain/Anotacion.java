package cl.sige.plataforma.ms_anotaciones.domain;

import java.time.LocalDateTime;

import cl.sige.plataforma.ms_anotaciones.domain.enums.CategoriaAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.EstadoAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.GravedadAnotacion;
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
@Table(name = "anotacion")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Anotacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anotacion_id")
    private Long id;

    //Referencia cruzada a ESTUDIANTE en ms-estudiantes
    @Column(name = "estudiante_id", nullable = false)
    private Long estudianteId;

    //Referencia cruzada a PERSONA_ROL en ms-identidad-acceso
    @Column(name = "autor_persona_rol_id", nullable = false)
    private Long autorPersonaRolId;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false, length = 20)
    private CategoriaAnotacion categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "gravedad", nullable = false, length = 20)
    private GravedadAnotacion gravedad;

    @Column(name = "descripcion", nullable = false, length = 1000)
    private String descripcion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoAnotacion estado;

    public Anotacion(Long estudianteId, Long autorPersonaRolId, CategoriaAnotacion categoria, GravedadAnotacion gravedad, String descripcion) {
        this.estudianteId = estudianteId;
        this.autorPersonaRolId = autorPersonaRolId;
        this.categoria = categoria;
        this.gravedad = gravedad;
        this.descripcion = descripcion;
        this.fechaCreacion = LocalDateTime.now();
        this.estado = EstadoAnotacion.VIGENTE;
    }

    public void modificar(String nuevaDescripcion) {
        this.descripcion = nuevaDescripcion;
    }

    public void anular() {
        this.estado = EstadoAnotacion.ANULADA;
    }

}
