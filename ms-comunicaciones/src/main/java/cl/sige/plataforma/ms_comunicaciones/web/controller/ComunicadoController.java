package cl.sige.plataforma.ms_comunicaciones.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_comunicaciones.domain.Comunicado;
import cl.sige.plataforma.ms_comunicaciones.service.ComunicadoService;
import cl.sige.plataforma.ms_comunicaciones.web.dto.comunicado.ComunicadoResponse;
import cl.sige.plataforma.ms_comunicaciones.web.dto.comunicado.CrearComunicadoRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comunicados")
public class ComunicadoController {
    
    private final ComunicadoService comunicadoService;

    @PostMapping
    public ResponseEntity<ComunicadoResponse> crear(@RequestBody CrearComunicadoRequest request) {
        Comunicado comunicado = comunicadoService.crear(
            request.emisorPersonaRolId(),
            request.tipoAlcance(),
            request.cursoId(),
            request.nivelEducativoId(),
            request.asunto(),
            request.contenido());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(comunicado));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunicadoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(comunicadoService.obtenerPorId(id)));
    }


    private ComunicadoResponse aResponse(Comunicado c) {
        return new ComunicadoResponse(c.getId(), c.getEmisorPersonaRolId(), c.getTipoAlcance(),
                c.getCursoId(), c.getNivelEducativoId(), c.getAsunto(), c.getContenido(), c.getFechaPublicacion());
    }

}
