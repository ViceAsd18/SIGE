package cl.sige.plataforma.ms_academico.domain;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curso")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "curso_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nivel_educativo_id", nullable = false)
    private NivelEducativo nivelEducativo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "periodo_academico_id", nullable = false)
    private PeriodoAcademico periodoAcademico;

    @Column(name = "paralelo", nullable = false, length = 5)
    private String paralelo;

    @Column(name = "profesor_jefe_persona_rol_id", nullable = false)
    private Long profesorJefePersonaRolId;
}
