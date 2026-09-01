package cl.sige.plataforma.ms_calendario_reuniones.domain;

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
@Table(name = "reunion_participante")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReunionParticipante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reunion_participante_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reunion_id", nullable = false)
    private Reunion reunion;

    @Column(name = "persona_id", nullable = false)
    private Long personaId;

    public ReunionParticipante (Reunion reunion, Long personaId) {
        this.reunion = reunion;
        this.personaId = personaId;
    }

}

