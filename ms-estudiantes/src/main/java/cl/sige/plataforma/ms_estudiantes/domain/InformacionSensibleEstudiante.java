package cl.sige.plataforma.ms_estudiantes.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "informacion_sensible_estudiante")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class InformacionSensibleEstudiante {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "informacion_sensible_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estudiante_id", nullable = false, unique = true)
    private Estudiante estudiante;
    
    @Setter
    @Column(name = "informacion_salud", columnDefinition = "TEXT")
    private String informacionSalud;

    @Setter
    @Column(name = "informacion_nee", columnDefinition = "TEXT")
    private String informacionNee;

    @Setter
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

}
