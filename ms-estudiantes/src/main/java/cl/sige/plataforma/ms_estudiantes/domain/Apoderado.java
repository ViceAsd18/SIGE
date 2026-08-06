package cl.sige.plataforma.ms_estudiantes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "apoderado")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Apoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apoderado_id")
    private Long id;
    
    @Column(name = "persona_rol_id", nullable = false, unique = true)
    private Long personaRolId;

    public Apoderado(Long personaRolId) {
        this.personaRolId = personaRolId;
    }

}
