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
@Table(name = "estudiante")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Estudiante {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "estudiante_id")
    private Long id;

    @Column(name = "persona_rol_id", nullable = false, unique = true)
    private Long personaRolId;

    public Estudiante(Long personaRolId) {
        this.personaRolId = personaRolId;
    }

}
