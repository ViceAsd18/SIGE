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
@Table(name = "conversacion_participante")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConversacionParticipante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conversacion_participante_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conversacion_id", nullable = false)
    private Conversacion conversacion;

    //Referencia cruzada a PERSONA en ms-identidad-acceso
    private Long personaId;

    @Column(name = "fecha_incorporacion", nullable = false, updatable = false)
    private LocalDateTime fechaIncorporacion;

    public ConversacionParticipante(Conversacion conversacion, Long personaId) {
        this.conversacion = conversacion;
        this.personaId = personaId;
        this.fechaIncorporacion = LocalDateTime.now();
    }

}
