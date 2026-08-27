package cl.sige.plataforma.ms_comunicaciones.domain;

import java.time.LocalDateTime;

import cl.sige.plataforma.ms_comunicaciones.domain.enums.TipoAlcanceComunicado;
import jakarta.annotation.Generated;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "comunicado")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Comunicado {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comunicado_id")
    private Long id;

    //Referencia cruzada a PERSONA_ROL en ms-identidad-acceso
    @Column(name = "emisor_persona_rol_id", nullable = false)
    private Long emisorPersonaRolId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_alcance", nullable = false, length = 20)
    private TipoAlcanceComunicado tipoAlcance;

    //Referencia cruzada a ms-academico
    @Column(name = "curso_id")
    private Long cursoId;

    @Column(name = "nivel_educativo_id")
    private Long nivelEducativoId;

    @Column(name = "asunto", nullable = false, length = 200)
    private String asunto;

    @Column(name = "contenido", nullable = false, length = 3000)
    private String contenido;

    @Column(name = "fecha_publicacion", nullable = false)
    private LocalDateTime fechaPublicacion;

}
