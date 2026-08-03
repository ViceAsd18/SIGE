package cl.sige.plataforma.ms_identidad_acceso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_identidad_acceso.domain.EstadoRol;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;

import java.util.List;

public interface PersonaRolRepository extends JpaRepository<PersonaRol, Long> {
    List<PersonaRol> findByPersonaIdAndEstado(Long personaId, EstadoRol estado);
}