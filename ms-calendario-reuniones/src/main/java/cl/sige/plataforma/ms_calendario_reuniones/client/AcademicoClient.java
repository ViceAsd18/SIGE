package cl.sige.plataforma.ms_calendario_reuniones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.sige.plataforma.ms_calendario_reuniones.client.dto.CursoClientResponse;

@FeignClient(name = "ms-academico")
public interface AcademicoClient {
    
    @GetMapping("/cursos/{id}")
    CursoClientResponse obtenerCurso(@PathVariable("id") Long id);

}
