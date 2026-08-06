package cl.sige.plataforma.ms_estudiantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_estudiantes.domain.Estudiante;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    Optional<Estudiante> findByPersonaRolId(Long personaRolId);
    boolean existsByPersonaRolId(Long personaRolId);
}