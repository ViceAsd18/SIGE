package cl.sige.plataforma.ms_evaluaciones_notas.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_evaluaciones_notas.service.SolicitudExcepcionCalificacionService;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud.AprobarSolicitudRequest;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud.CrearSolicitudRequest;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud.SolicitudResponse;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.SolicitudExcepcionCalificacion;

@RestController
@RequestMapping("/solicitudes-exception-calificacion")
@RequiredArgsConstructor
public class SolicitudExcepcionCalificacionController {
    
    private final SolicitudExcepcionCalificacionService solicitudService;

    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(@RequestBody CrearSolicitudRequest request) {
        SolicitudExcepcionCalificacion solicitud =  solicitudService.crear(
            request.calificacionId(), request.solicitantePersonaRolId(), request.motivo());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(solicitud));
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudResponse> aprobar(@PathVariable Long id, @RequestBody AprobarSolicitudRequest request) {
        SolicitudExcepcionCalificacion solicitud = solicitudService.aprobar(
            id, request.aprobadorPersonaRolId(), request.nuevoResultado());
        return ResponseEntity.ok(aResponse(solicitud));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudResponse> rechazar(@PathVariable Long id, @RequestBody AprobarSolicitudRequest request) {
        SolicitudExcepcionCalificacion solicitud = solicitudService.rechazar(id, request.aprobadorPersonaRolId());
        return ResponseEntity.ok(aResponse(solicitud));
    }



    private SolicitudResponse aResponse(SolicitudExcepcionCalificacion s) {
        return new SolicitudResponse(s.getId(), s.getCalificacion().getId(), s.getSolicitudPersonaRolId(),
                s.getAprobadorPersonaRolId(), s.getMotivo(), s.getEstado().name());
    }

}
