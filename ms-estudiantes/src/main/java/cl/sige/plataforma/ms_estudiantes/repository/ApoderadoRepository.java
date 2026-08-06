package cl.sige.plataforma.ms_estudiantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_estudiantes.domain.Apoderado;

import java.util.Optional;

public interface ApoderadoRepository extends JpaRepository<Apoderado, Long> {
    Optional<Apoderado> findByPersonaRolId(Long personaRolId);
    boolean existsByPersonaRolId(Long personaRolId);
}