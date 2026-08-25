package cl.sige.plataforma.ms_mensajeria.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_mensajeria.domain.Conversacion;
import cl.sige.plataforma.ms_mensajeria.service.ConversacionService;
import cl.sige.plataforma.ms_mensajeria.web.dto.conversacion.ConversacionResponse;
import cl.sige.plataforma.ms_mensajeria.web.dto.conversacion.IniciarConversacionDocenteApoderadoRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/conversaciones")
@RequiredArgsConstructor
public class ConversacionController {
    
    private final ConversacionService conversacionService;

    @PostMapping("/docente-apoderado")
    public ResponseEntity<ConversacionResponse> iniciarDocenteApoderado(@RequestBody IniciarConversacionDocenteApoderadoRequest request) {
        Conversacion conversacion = conversacionService.iniciarDocenteApoderado(
            request.docentePersonaId(),
            request.docentePersonaRolId(),
            request.apoderadoPersonaId(),
            request.apoderadoPersonaRolId(), 
            request.estudianteId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(new ConversacionResponse(conversacion.getId(),conversacion.getFechaInicio()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConversacionResponse> obtener(@PathVariable Long id) {
        Conversacion conversacion = conversacionService.obtenerPorId(id);
        return ResponseEntity.ok(new ConversacionResponse(conversacion.getId(),conversacion.getFechaInicio()));
    }

}
