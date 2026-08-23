package cl.sige.plataforma.ms_anotaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.sige.plataforma.ms_anotaciones.client.dto.EstudianteClientResponse;

@FeignClient(name = "ms-estudiantes")
public interface EstudiantesClient {
    
    @GetMapping("/estudiantes/{id}")
    EstudianteClientResponse obtenerEstudiante(@PathVariable("id") Long id);

}
