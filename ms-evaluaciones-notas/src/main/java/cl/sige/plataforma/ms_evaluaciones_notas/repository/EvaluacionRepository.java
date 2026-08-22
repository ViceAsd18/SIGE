package cl.sige.plataforma.ms_evaluaciones_notas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_evaluaciones_notas.domain.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    
    List<Evaluacion> findByAsignacionDocenteId(Long asignacionDocenteId);
    List<Evaluacion> findBySubperiodoAcademicoId(Long subperiodoAcademicoId);
}
