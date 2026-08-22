package cl.sige.plataforma.ms_academico.web.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_academico.domain.AsignacionDocente;
import cl.sige.plataforma.ms_academico.service.AsignacionDocenteService;
import cl.sige.plataforma.ms_academico.web.dto.asignacion_docente.AsignacionDocenteResponse;
import cl.sige.plataforma.ms_academico.web.dto.asignacion_docente.CrearAsignacionDocenteRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/asignaciones-docente")
@RequiredArgsConstructor
public class AsignacionDocenteController {
    
    private final AsignacionDocenteService asignacionDocenteService;

    @PostMapping
    public ResponseEntity<AsignacionDocenteResponse> crear(@RequestBody CrearAsignacionDocenteRequest request) {
        AsignacionDocente asignacion = asignacionDocenteService.crear(
                request.docentePersonaRolId(), request.asignaturaId(),
                request.cursoId(), request.fechaInicioVigencia());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(asignacion));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable Long id, @RequestBody Map<String, LocalDate> body) {
        asignacionDocenteService.finalizarVigencia(id, body.get("fechaTermino"));
        return ResponseEntity.noContent().build();
    }

    private AsignacionDocenteResponse aResponse(AsignacionDocente a) {
        return new AsignacionDocenteResponse(
                a.getId(), a.getDocentePersonaRolId(), a.getAsignatura().getId(), a.getCurso().getId(),
                a.getFechaInicioVigencia(), a.getFechaTerminoVigencia(), a.getEstado());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionDocenteResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(asignacionDocenteService.obtenerPorId(id)));
    }


}
