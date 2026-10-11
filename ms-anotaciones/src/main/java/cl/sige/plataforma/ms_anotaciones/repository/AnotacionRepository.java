package cl.sige.plataforma.ms_anotaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cl.sige.plataforma.ms_anotaciones.domain.Anotacion;

public interface AnotacionRepository extends JpaRepository<Anotacion, Long>, JpaSpecificationExecutor<Anotacion> {
    
    List<Anotacion> findByEstudianteId(Long estudianteId);

}
