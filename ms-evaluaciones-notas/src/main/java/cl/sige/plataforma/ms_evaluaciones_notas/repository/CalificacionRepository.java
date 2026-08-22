package cl.sige.plataforma.ms_evaluaciones_notas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.sige.plataforma.ms_evaluaciones_notas.domain.Calificacion;
import feign.Param;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long> {
    
    boolean existsByEvaluacionIdAndEstudianteId(Long evaluacionId, Long estudianteId);
    @Query("SELECT c FROM Calificacion c JOIN FETCH c.evaluacion WHERE c.id = :id")
    Optional<Calificacion> findByIdConEvaluacion(@Param("id") Long id);

    @Query("SELECT c FROM Calificacion c JOIN FETCH c.evaluacion WHERE c.estudianteId = :estudianteId")
    List<Calificacion> findByEstudianteIdConEvaluacion(@Param("estudianteId") Long estudianteId);

}
