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

import cl.sige.plataforma.ms_mensajeria.domain.Mensaje;
import cl.sige.plataforma.ms_mensajeria.service.MensajeService;
import cl.sige.plataforma.ms_mensajeria.web.dto.mensajes.EditarMensajeRequest;
import cl.sige.plataforma.ms_mensajeria.web.dto.mensajes.EnviarMensajeRequest;
import cl.sige.plataforma.ms_mensajeria.web.dto.mensajes.MensajeResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/conversaciones/{conversacionId}/mensajes")
public class MensajeController {
    
    private final MensajeService mensajeService;

    @PostMapping
    public ResponseEntity<MensajeResponse> enviar(@PathVariable Long conversacionId, @RequestBody EnviarMensajeRequest request) {
        Mensaje mensaje = mensajeService.enviar(conversacionId, request.autorPersonaId(), request.contenido());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(mensaje));
    }

    @GetMapping
    public ResponseEntity<List<MensajeResponse>> listar (@PathVariable Long conversacionId) {
        List<MensajeResponse> response = mensajeService.obtenerPorConversacion(conversacionId).stream().map(this::aResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{mensajeId}")
    public ResponseEntity<Void> editar(@PathVariable Long conversacionId, @PathVariable Long mensajeId, @RequestBody EditarMensajeRequest request) {
        mensajeService.editar(mensajeId, request.autorPersonaId(), request.nuevoContenido());
        return ResponseEntity.noContent().build();
    }

    private MensajeResponse aResponse(Mensaje m) {
        return new MensajeResponse(m.getId(), m.getConversacion().getId(), m.getAutorPersonaId(),
                m.getContenido(), m.getFechaEnvio(), m.getFechaUltimaModificacion());
    }

}
