package cl.sige.plataforma.ms_mensajeria.domain;

import java.time.LocalDateTime;

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
@Table(name = "mensaje")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mensaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mensaje_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversacion_id", nullable = false)
    private Conversacion conversacion;

    //Referencia cruzada a PERSONA en ms-identidad-acceso
    @Column(name = "autor_persona_id", nullable = false)
    private Long autorPersonaId;

    @Column(name = "contenido", nullable = false, length = 2000)
    private String contenido;

    @Column(name = "fecha_envio", nullable = false, updatable = false)
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_ultima_modificacion")
    private LocalDateTime fechaUltimaModificacion;

    public Mensaje(Conversacion conversacion, Long autorPersonaId, String contenido) {
        this.conversacion = conversacion;
        this.autorPersonaId = autorPersonaId;
        this.contenido = contenido;
        this.fechaEnvio = LocalDateTime.now();
    }

    public void editar(String nuevoContenido) {
        this.contenido = nuevoContenido;
        this.fechaUltimaModificacion = LocalDateTime.now();
    }


}
