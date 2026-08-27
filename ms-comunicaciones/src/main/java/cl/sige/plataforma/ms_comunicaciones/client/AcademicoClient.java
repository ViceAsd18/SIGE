package cl.sige.plataforma.ms_comunicaciones.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.sige.plataforma.ms_comunicaciones.client.dto.CursoClientResponse;
import cl.sige.plataforma.ms_comunicaciones.client.dto.NivelEducativoClientResponse;

@FeignClient(name = "ms-academico")
public interface AcademicoClient {
    
    @GetMapping("/cursos/{id}")
    CursoClientResponse obtenerCurso(@PathVariable("id") Long id);

    @GetMapping("/niveles-educativos/{id}")
    NivelEducativoClientResponse obtenerNivel(@PathVariable("id") Long id);

}
