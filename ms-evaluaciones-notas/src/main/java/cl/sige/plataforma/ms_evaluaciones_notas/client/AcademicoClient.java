package cl.sige.plataforma.ms_evaluaciones_notas.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.sige.plataforma.ms_evaluaciones_notas.client.dto.AsignacionDocenteClientResponse;
import cl.sige.plataforma.ms_evaluaciones_notas.client.dto.SubperiodoAcademicoClientResponse;


@FeignClient(name = "ms-academico")
public interface AcademicoClient {
    
    @GetMapping("/asignaciones-docente/{id}")
    AsignacionDocenteClientResponse obtenerAsignacionDocente(@PathVariable("id") Long id);

    @GetMapping("/subperiodos-academicos/{id}")
    SubperiodoAcademicoClientResponse obtenerSubperiodo(@PathVariable("id") Long id);

}
