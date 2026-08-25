package cl.sige.plataforma.ms_mensajeria.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_mensajeria.domain.SolicitudAccesoConversacion;
import cl.sige.plataforma.ms_mensajeria.service.SolicitudAccesoConversacionService;
import cl.sige.plataforma.ms_mensajeria.web.dto.mensajes.MensajeResponse;
import cl.sige.plataforma.ms_mensajeria.web.dto.solicitud.CrearSolicitudAccesoRequest;
import cl.sige.plataforma.ms_mensajeria.web.dto.solicitud.ResolverSolicitudAccesoRequest;
import cl.sige.plataforma.ms_mensajeria.web.dto.solicitud.SolicitudAccesoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/solicitudes-acceso-conversacion")
@RequiredArgsConstructor
public class SolicitudAccesoConversacionController {
    
    private final SolicitudAccesoConversacionService solicitudService;

    @PostMapping
    public ResponseEntity<SolicitudAccesoResponse> crear(@RequestBody CrearSolicitudAccesoRequest request) {
        SolicitudAccesoConversacion s = solicitudService.crear(
                request.conversacionId(), request.solicitantePersonaRolId(), request.motivo());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(s));
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<SolicitudAccesoResponse> aprobar(@PathVariable Long id, @RequestBody ResolverSolicitudAccesoRequest request) {
        SolicitudAccesoConversacion s = solicitudService.aprobar(id, request.aprobadorPersonaRolId());
        return ResponseEntity.ok(aResponse(s));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudAccesoResponse> rechazar(@PathVariable Long id,@RequestBody ResolverSolicitudAccesoRequest request) {
        SolicitudAccesoConversacion s = solicitudService.rechazar(id, request.aprobadorPersonaRolId());
        return ResponseEntity.ok(aResponse(s));
    }

    @GetMapping("/{id}/mensajes")
    public ResponseEntity<List<MensajeResponse>> obtenerMensajes(@PathVariable Long id) {
        List<MensajeResponse> response = solicitudService.obtenerMensajesConAcceso(id).stream()
                .map(m -> new MensajeResponse(m.getId(), m.getConversacion().getId(), m.getAutorPersonaId(),
                        m.getContenido(), m.getFechaEnvio(), m.getFechaUltimaModificacion()))
                .toList();
        return ResponseEntity.ok(response);
    }

    private SolicitudAccesoResponse aResponse(SolicitudAccesoConversacion s) {
        return new SolicitudAccesoResponse(s.getId(), s.getConversacion().getId(), s.getSolicitantePersonaRolId(),
                s.getAprobadorPersonaRolId(), s.getMotivo(), s.getEstado().name());
    }


}
