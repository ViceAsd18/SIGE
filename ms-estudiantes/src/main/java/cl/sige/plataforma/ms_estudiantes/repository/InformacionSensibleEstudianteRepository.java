package cl.sige.plataforma.ms_estudiantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_estudiantes.domain.InformacionSensibleEstudiante;

import java.util.Optional;

public interface InformacionSensibleEstudianteRepository extends JpaRepository<InformacionSensibleEstudiante, Long> {
    Optional<InformacionSensibleEstudiante> findByEstudianteId(Long estudianteId);
}
