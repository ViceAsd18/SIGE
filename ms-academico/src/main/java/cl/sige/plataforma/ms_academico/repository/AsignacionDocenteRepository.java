package cl.sige.plataforma.ms_academico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_academico.domain.AsignacionDocente;

public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {

    List<AsignacionDocente> findByDocentePersonaRolId(Long docentePersonaRolId);

    List<AsignacionDocente> findByCursoId(Long cursoId);

}