package cl.sige.plataforma.ms_estudiantes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.sige.plataforma.ms_estudiantes.domain.ApoderadoEstudiante;
import feign.Param;

import java.util.List;

public interface ApoderadoEstudianteRepository extends JpaRepository<ApoderadoEstudiante, Long> {
    boolean existsByApoderadoIdAndEstudianteId(Long apoderadoId, Long estudianteId);

   @Query("SELECT ae FROM ApoderadoEstudiante ae JOIN FETCH ae.estudiante WHERE ae.apoderado.id = :apoderadoId")
    List<ApoderadoEstudiante> findByApoderadoIdConEstudiante(@Param("apoderadoId") Long apoderadoId);

    @Query("SELECT ae FROM ApoderadoEstudiante ae JOIN FETCH ae.apoderado WHERE ae.estudiante.id = :estudianteId")
    List<ApoderadoEstudiante> findByEstudianteIdConApoderado(@Param("estudianteId") Long estudianteId);


    
}
