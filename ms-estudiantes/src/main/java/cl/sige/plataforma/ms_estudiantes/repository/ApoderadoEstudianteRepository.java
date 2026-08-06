package cl.sige.plataforma.ms_estudiantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_estudiantes.domain.ApoderadoEstudiante;

import java.util.List;

public interface ApoderadoEstudianteRepository extends JpaRepository<ApoderadoEstudiante, Long> {
    List<ApoderadoEstudiante> findByApoderadoId(Long apoderadoId);
    List<ApoderadoEstudiante> findByEstudianteId(Long estudianteId);
    boolean existsByApoderadoIdAndEstudianteId(Long apoderadoId, Long estudianteId);
}
