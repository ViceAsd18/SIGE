package cl.sige.plataforma.ms_auditoria.domain;

import java.time.LocalDateTime;

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
@Table(name = "registro_auditoria")
@Getter 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder 
public class RegistroAuditoria {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registro_auditoria_id")
    private Long id;

    //Referencia cruzada a PERSONA en ms-identidad-acceso
    @Column(name = "persona_id", nullable = false)
    private Long personaId;

    //Referencia cruzada a PERSONA_ROL en ms-identidad-acceso
    @Column(name = "rol_activo_persona_rol_id")
    private Long rolActivoPersonaRolId;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "accion", nullable = false, length = 50)
    private String accion;

    //Referencia generica
    @Column(name = "entidad_afectada_tipo", nullable = false, length = 50)
    private String entidadAfectadaTipo;

    @Column(name = "entidad_afectada_id", nullable = false)
    private Long entidadAfectadaId;

    @Column(name = "valor_anterior", length = 2000)
    private String valorAnterior;

    @Column(name = "valor_nuevo", length = 2000)
    private String valorNuevo;

    @Column(name = "motivo", length = 500)
    private String motivo;

}
