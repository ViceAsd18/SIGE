package cl.sige.plataforma.ms_identidad_acceso.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_identidad_acceso.domain.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombreRol(String nombreRol);
}