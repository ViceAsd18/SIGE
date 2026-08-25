package cl.sige.plataforma.ms_mensajeria.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cl.sige.plataforma.ms_mensajeria.client.dto.AsignacionDocenteClientResponse;


@FeignClient(name = "ms-academico")
public interface AcademicoClient {
    
    @GetMapping("/asignaciones-docente")
    List<AsignacionDocenteClientResponse> buscarPorDocente(@RequestParam("docentePersonaRolId") Long docentePersonaRolId);

}
