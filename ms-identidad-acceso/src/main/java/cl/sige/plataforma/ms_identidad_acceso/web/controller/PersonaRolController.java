package cl.sige.plataforma.ms_identidad_acceso.web.controller;

import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import cl.sige.plataforma.ms_identidad_acceso.service.PersonaRolService;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.persona_rol.PersonaRolResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/persona-roles")
@RequiredArgsConstructor
public class PersonaRolController {

    private final PersonaRolService personaRolService;

    @GetMapping("/{id}")
    public ResponseEntity<PersonaRolResponse> obtener(@PathVariable Long id) {
        PersonaRol pr = personaRolService.obtenerPorId(id);
        return ResponseEntity.ok(new PersonaRolResponse(
                pr.getId(), 
                pr.getPersona().getId(), 
                pr.getRol().getNombreRol(),
                pr.getEstado().name(), 
                pr.getFechaInicio(), 
                pr.getFechaTermino()
        ));
    }
}   