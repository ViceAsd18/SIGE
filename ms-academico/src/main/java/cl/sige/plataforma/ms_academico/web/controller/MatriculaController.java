package cl.sige.plataforma.ms_academico.web.controller;

import cl.sige.plataforma.ms_academico.domain.Matricula;
import cl.sige.plataforma.ms_academico.service.MatriculaService;
import cl.sige.plataforma.ms_academico.web.dto.matricula.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matriculas")
@RequiredArgsConstructor
public class MatriculaController {

    private final MatriculaService matriculaService;

    @PostMapping
    public ResponseEntity<MatriculaResponse> crear(@RequestBody CrearMatriculaRequest request) {
        Matricula matricula = matriculaService.crear(
                request.estudianteId(), request.cursoId(), request.fechaInicioVigencia());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(matricula));
    }

    @PatchMapping("/{id}/cambiar-curso")
    public ResponseEntity<MatriculaResponse> cambiarCurso(@PathVariable Long id,
                                                            @RequestBody CambiarCursoRequest request) {
        Matricula nueva = matriculaService.cambiarCurso(id, request.nuevoCursoId(), request.motivo());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(nueva));
    }

    @PatchMapping("/{id}/retirar")
    public ResponseEntity<Void> retirar(@PathVariable Long id, @RequestBody RetirarMatriculaRequest request) {
        matriculaService.retirar(id, request.motivo());
        return ResponseEntity.noContent().build();
    }

    private MatriculaResponse aResponse(Matricula m) {
        return new MatriculaResponse(
                m.getId(), m.getEstudianteId(), m.getCurso().getId(), m.getEstado(),
                m.getMotivo(), m.getFechaInicioVigencia(), m.getFechaTerminoVigencia());
    }
}