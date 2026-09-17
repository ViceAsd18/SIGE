package cl.sige.plataforma.ms_identidad_acceso.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.sige.plataforma.ms_identidad_acceso.domain.EstadoRol;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;

import java.util.List;
import java.util.Optional;

public interface PersonaRolRepository extends JpaRepository<PersonaRol, Long> {
    List<PersonaRol> findByPersonaIdAndEstado(Long personaId, EstadoRol estado);
    @Query("SELECT pr FROM PersonaRol pr JOIN FETCH pr.persona JOIN FETCH pr.rol WHERE pr.id = :id")
    Optional<PersonaRol> findByIdConPersonaYRol(@Param("id") Long id);

    @Query("SELECT pr FROM PersonaRol pr JOIN FETCH pr.rol WHERE pr.persona.id = :personaId AND pr.estado = :estado")
    List<PersonaRol> findByPersonaIdAndEstadoConRol(@Param("personaId") Long personaId, @Param("estado") EstadoRol estado);

}