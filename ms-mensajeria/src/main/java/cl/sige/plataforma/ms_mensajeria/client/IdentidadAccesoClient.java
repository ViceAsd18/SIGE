package cl.sige.plataforma.ms_mensajeria.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.sige.plataforma.ms_mensajeria.client.dto.PersonaRolClientResponse;

@FeignClient(name = "ms-identidad-acceso")
public interface IdentidadAccesoClient {

    @GetMapping("/persona-roles/{id}")
    public PersonaRolClientResponse obtenerPersonaRol(@PathVariable("id") Long id);
}


