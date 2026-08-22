package cl.sige.plataforma.ms_evaluaciones_notas.web.controller;

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

import cl.sige.plataforma.ms_evaluaciones_notas.domain.Calificacion;
import cl.sige.plataforma.ms_evaluaciones_notas.service.CalificacionService;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.calificacion.CalificacionResponse;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.calificacion.CrearCalificacionRequest;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.calificacion.ModificarCalificacionRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calificaciones")
@RequiredArgsConstructor
public class CalificacionController {
    
    private final CalificacionService calificacionService;
    
    @PostMapping
    public ResponseEntity<CalificacionResponse> crear(@RequestBody CrearCalificacionRequest request) {
        Calificacion calificacion = calificacionService.crear(
            request.evaluacionId(), request.estudianteId(), request.resultado());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(calificacion));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> modificar(@PathVariable Long id, @RequestBody ModificarCalificacionRequest request) {
        calificacionService.modificar(id, request.resultado());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<CalificacionResponse>> obtenerPorEstudiante(@PathVariable Long estudianteId) {
        List<CalificacionResponse> response = calificacionService
            .obtenerPorEstudiante(estudianteId).stream().map(this::aResponse).toList();
        return ResponseEntity.ok(response);
    }

    private CalificacionResponse aResponse(Calificacion c) {
        return new CalificacionResponse(c.getId(), c.getEvaluacion().getId(), c.getEstudianteId(), c.getResultado());
    }

}
