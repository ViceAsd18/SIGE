package cl.sige.plataforma.ms_identidad_acceso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByUsuario(String usuario);
    Optional<Persona> findByEmail(String email);
    Optional<Persona> findByRutDocumentoIdentidad(String rut);
}