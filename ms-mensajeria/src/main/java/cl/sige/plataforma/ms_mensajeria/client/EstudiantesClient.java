package cl.sige.plataforma.ms_mensajeria.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.sige.plataforma.ms_mensajeria.client.dto.ApoderadoRelacionClientResponse;

@FeignClient(name = "ms-estudiantes")
public interface EstudiantesClient {

    @GetMapping("/estudiantes/{estudianteId}/apoderados")
    public List<ApoderadoRelacionClientResponse> obtenerApoderadosDeEstudiante(@PathVariable("estudianteId") Long estudianteId);
    
}
