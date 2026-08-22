package cl.sige.plataforma.ms_evaluaciones_notas.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_evaluaciones_notas.domain.Evaluacion;
import cl.sige.plataforma.ms_evaluaciones_notas.service.EvaluacionService;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.evaluacion.CrearEvaluacionRequest;
import cl.sige.plataforma.ms_evaluaciones_notas.web.dto.evaluacion.EvaluacionResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {
    
    private final EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<EvaluacionResponse> crear(@RequestBody CrearEvaluacionRequest request) {
        Evaluacion evaluacion = evaluacionService.crear(
            request.asignacionDocenteId(), request.subperiodoAcademicoId(),
            request.nombre(), request.fecha(), request.ponderacion());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(evaluacion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(evaluacionService.obtenerPorId(id)));
    }

    private EvaluacionResponse aResponse(Evaluacion e) {
        return new EvaluacionResponse(e.getId(), e.getAsignacionDocenteId(),
                e.getSubperiodoAcademicoId(), e.getNombre(), e.getFecha(), e.getPonderacion());
    }


}
