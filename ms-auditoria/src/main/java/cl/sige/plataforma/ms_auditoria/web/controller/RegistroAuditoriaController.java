package cl.sige.plataforma.ms_auditoria.web.controller;

import cl.sige.plataforma.ms_auditoria.domain.RegistroAuditoria;
import cl.sige.plataforma.ms_auditoria.service.RegistroAuditoriaService;
import cl.sige.plataforma.ms_auditoria.web.dto.CrearRegistroAuditoriaRequest;
import cl.sige.plataforma.ms_auditoria.web.dto.RegistroAuditoriaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auditoria")
@RequiredArgsConstructor
public class RegistroAuditoriaController {

    private final RegistroAuditoriaService registroAuditoriaService;

    @PostMapping
    public ResponseEntity<RegistroAuditoriaResponse> registrar(@RequestBody CrearRegistroAuditoriaRequest request) {
        RegistroAuditoria r = registroAuditoriaService.registrar(
                request.personaId(), request.rolActivoPersonaRolId(), request.accion(),
                request.entidadAfectadaTipo(), request.entidadAfectadaId(),
                request.valorAnterior(), request.valorNuevo(), request.motivo());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(r));
    }

    @GetMapping
    public ResponseEntity<List<RegistroAuditoriaResponse>> consultar(
            @RequestParam(value = "entidadTipo", required = false) String entidadTipo,
            @RequestParam(value = "entidadId", required = false) Long entidadId,
            @RequestParam(value = "personaId", required = false) Long personaId) {

        List<RegistroAuditoria> registros;
        if (entidadTipo != null && entidadId != null) {
            registros = registroAuditoriaService.consultarPorEntidad(entidadTipo, entidadId);
        } else if (personaId != null) {
            registros = registroAuditoriaService.consultarPorPersona(personaId);
        } else {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(registros.stream().map(this::aResponse).toList());
    }

    private RegistroAuditoriaResponse aResponse(RegistroAuditoria r) {
        return new RegistroAuditoriaResponse(r.getId(), r.getPersonaId(), r.getRolActivoPersonaRolId(),
                r.getFecha(), r.getAccion(), r.getEntidadAfectadaTipo(), r.getEntidadAfectadaId(),
                r.getValorAnterior(), r.getValorNuevo(), r.getMotivo());
    }
}