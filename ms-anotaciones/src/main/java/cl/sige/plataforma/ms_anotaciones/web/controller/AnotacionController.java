package cl.sige.plataforma.ms_anotaciones.web.controller;

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

import cl.sige.plataforma.ms_anotaciones.domain.Anotacion;
import cl.sige.plataforma.ms_anotaciones.service.AnotacionService;
import cl.sige.plataforma.ms_anotaciones.web.dto.AnotacionResponse;
import cl.sige.plataforma.ms_anotaciones.web.dto.CrearAnotacionRequest;
import cl.sige.plataforma.ms_anotaciones.web.dto.ModificarAnotacionRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/anotaciones")
@RequiredArgsConstructor
public class AnotacionController {
    
    private final AnotacionService anotacionService;

    @PostMapping
    public ResponseEntity<AnotacionResponse> crear(@RequestBody CrearAnotacionRequest request) {
        Anotacion anotacion = anotacionService.crear(
            request.estudianteId(), request.autorPersonaRolId(), 
            request.categoria(), request.gravedad(), request.descripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(anotacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnotacionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(anotacionService.obtenerPorId(id)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> modificar(@PathVariable Long id, @RequestBody ModificarAnotacionRequest request) {
        anotacionService.modificar(id, request.descripcion());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/anular")
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        anotacionService.anular(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<AnotacionResponse>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        List<AnotacionResponse> response = anotacionService.obtenerPorEstudiante(estudianteId)
                .stream()
                .map(this::aResponse)
                .toList();
        return ResponseEntity.ok(response);
    }



    private AnotacionResponse aResponse(Anotacion a) {
        return new AnotacionResponse(a.getId(), a.getEstudianteId(), a.getAutorPersonaRolId(),
                a.getCategoria(), a.getGravedad(), a.getDescripcion(), a.getFechaCreacion(), a.getEstado());
    }

}
