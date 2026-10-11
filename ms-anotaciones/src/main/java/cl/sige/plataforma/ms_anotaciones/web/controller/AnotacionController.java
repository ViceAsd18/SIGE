package cl.sige.plataforma.ms_anotaciones.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_anotaciones.domain.Anotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.CategoriaAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.EstadoAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.GravedadAnotacion;
import cl.sige.plataforma.ms_anotaciones.service.AnotacionService;
import cl.sige.plataforma.ms_anotaciones.web.dto.AnotacionResponse;
import cl.sige.plataforma.ms_anotaciones.web.dto.AnularAnotacionRequest;
import cl.sige.plataforma.ms_anotaciones.web.dto.CrearAnotacionRequest;
import cl.sige.plataforma.ms_anotaciones.web.dto.ModificarAnotacionRequest;
import cl.sige.plataforma.ms_anotaciones.web.dto.PaginaResponse;
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
    public ResponseEntity<Void> anular(@PathVariable Long id, @RequestBody AnularAnotacionRequest request) {
        anotacionService.anular(id, request.anuladorPersonaRolId(), request.motivo());
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

    @GetMapping
    public ResponseEntity<PaginaResponse<AnotacionResponse>> buscar(
            @RequestParam(required = false) EstadoAnotacion estado,
            @RequestParam(required = false) CategoriaAnotacion categoria,
            @RequestParam(required = false) GravedadAnotacion gravedad,
            @RequestParam(required = false) Long estudianteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Anotacion> resultado = anotacionService.buscar(estado, categoria, gravedad, estudianteId, desde, hasta, page, size);

        PaginaResponse<AnotacionResponse> response = new PaginaResponse<>(
            resultado.getContent().stream().map(this::aResponse).toList(),
            resultado.getNumber(), resultado.getSize(), resultado.getTotalElements(), resultado.getTotalPages());
        return ResponseEntity.ok(response);
    }

    private AnotacionResponse aResponse(Anotacion a) {
        return new AnotacionResponse(a.getId(), a.getEstudianteId(), a.getAutorPersonaRolId(),
                a.getCategoria(), a.getGravedad(), a.getDescripcion(), a.getFechaCreacion(), a.getEstado(), a.getMotivoAnulacion());
    }

}
