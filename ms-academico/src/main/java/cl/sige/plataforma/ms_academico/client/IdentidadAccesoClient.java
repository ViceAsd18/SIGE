package cl.sige.plataforma.ms_academico.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.sige.plataforma.ms_academico.client.dto.PersonaRolClientResponse;

@FeignClient(name = "ms-identidad-acceso")
public interface IdentidadAccesoClient {
    
    @GetMapping("/persona-roles/{id}")
    PersonaRolClientResponse obtenerPersonaRol(@PathVariable("id") Long id);

}
