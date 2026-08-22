package cl.sige.plataforma.ms_academico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_academico.domain.PeriodoAcademico;

public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Long> {

    Optional<PeriodoAcademico> findByNombreAnio(String nombreAnio);
    boolean existsByNombreAnio(String nombreAnio);
    
}