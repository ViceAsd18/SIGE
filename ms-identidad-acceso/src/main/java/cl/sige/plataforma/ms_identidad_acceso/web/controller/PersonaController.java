package cl.sige.plataforma.ms_identidad_acceso.web.controller;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import cl.sige.plataforma.ms_identidad_acceso.service.NuevaPersonaComando;
import cl.sige.plataforma.ms_identidad_acceso.service.PersonaRolService;
import cl.sige.plataforma.ms_identidad_acceso.service.PersonaService;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.*;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.persona.CrearPersonaRequest;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.persona.PersonaResponse;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.rol.AsignarRolRequest;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.rol.RolActivoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;
    private final PersonaRolService personaRolService;

    @PostMapping
    public ResponseEntity<PersonaResponse> crear(@RequestBody CrearPersonaRequest request) {
        NuevaPersonaComando comando = new NuevaPersonaComando(
                request.rutDocumentoIdentidad(), request.nombres(), request.apellidos(),
                request.fechaNacimiento(), request.email(), request.telefono(),
                request.usuario(), request.password()
        );
        Persona persona = personaService.crear(comando);
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(persona));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(personaService.obtenerPorId(id)));
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<List<RolActivoResponse>> obtenerRolesActivos(@PathVariable Long id) {
        List<RolActivoResponse> response = personaRolService.obtenerRolesActivos(id).stream()
                .map(pr -> new RolActivoResponse(pr.getId(), pr.getRol().getNombreRol(), pr.getFechaInicio()))
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/roles")
    public ResponseEntity<RolActivoResponse> asignarRol(@PathVariable Long id,
                                                          @RequestBody AsignarRolRequest request) {
        PersonaRol personaRol = personaRolService.asignarRol(id, request.nombreRol());
        RolActivoResponse response = new RolActivoResponse(
                personaRol.getId(), personaRol.getRol().getNombreRol(), personaRol.getFechaInicio());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/roles/{personaRolId}/desactivar")
    public ResponseEntity<Void> desactivarRol(@PathVariable Long personaRolId) {
        personaRolService.desactivarRol(personaRolId);
        return ResponseEntity.noContent().build();
    }

    private PersonaResponse aResponse(Persona persona) {
        return new PersonaResponse(
                persona.getId(), persona.getRutDocumentoIdentidad(), persona.getNombres(),
                persona.getApellidos(), persona.getFechaNacimiento(), persona.getEmail(),
                persona.getTelefono(), persona.getUsuario()
        );
    }
}