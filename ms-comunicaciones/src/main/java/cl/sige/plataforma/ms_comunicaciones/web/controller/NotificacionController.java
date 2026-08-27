package cl.sige.plataforma.ms_comunicaciones.web.controller;

import cl.sige.plataforma.ms_comunicaciones.domain.Notificacion;
import cl.sige.plataforma.ms_comunicaciones.service.NotificacionService;
import cl.sige.plataforma.ms_comunicaciones.web.dto.notificacion.CrearNotificacionRequest;
import cl.sige.plataforma.ms_comunicaciones.web.dto.notificacion.NotificacionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping
    public ResponseEntity<NotificacionResponse> crear(@RequestBody CrearNotificacionRequest request) {
        Notificacion n = notificacionService.crear(request.destinatarioPersonaId(), request.tipoEvento(),
                request.entidadOrigenTipo(), request.entidadOrigenId(), request.contenido());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(n));
    }

    @PatchMapping("/{id}/marcar-leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        notificacionService.marcarLeida(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/destinatario/{destinatarioPersonaId}")
    public ResponseEntity<List<NotificacionResponse>> obtenerPorDestinatario(@PathVariable Long destinatarioPersonaId) {
        List<NotificacionResponse> response = notificacionService
                .obtenerPorDestinatario(destinatarioPersonaId).stream().map(this::aResponse).toList();
        return ResponseEntity.ok(response);
    }

    private NotificacionResponse aResponse(Notificacion n) {
        return new NotificacionResponse(n.getId(), n.getDestinatarioPersonaId(), n.getTipoEvento(),
                n.getContenido(), n.getEstado().name(), n.getFechaCreacion(), n.getFechaLectura());
    }
}