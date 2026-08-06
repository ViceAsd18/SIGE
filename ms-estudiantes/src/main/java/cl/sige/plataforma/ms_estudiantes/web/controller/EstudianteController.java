package cl.sige.plataforma.ms_estudiantes.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_estudiantes.domain.Estudiante;
import cl.sige.plataforma.ms_estudiantes.service.EstudianteService;
import cl.sige.plataforma.ms_estudiantes.web.dto.estudiante.CrearEstudianteRequest;
import cl.sige.plataforma.ms_estudiantes.web.dto.estudiante.EstudianteResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/estudiantes")
@RequiredArgsConstructor
public class EstudianteController {

    private final EstudianteService estudianteService;

    @PostMapping
    public ResponseEntity<EstudianteResponse> crear(@RequestBody CrearEstudianteRequest request) {
        Estudiante estudiante = estudianteService.crear(request.personaRolId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new EstudianteResponse(estudiante.getId(), estudiante.getPersonaRolId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponse> obtener(@PathVariable Long id) {
        Estudiante estudiante = estudianteService.obtenerPorId(id);
        return ResponseEntity.ok(new EstudianteResponse(estudiante.getId(), estudiante.getPersonaRolId()));
    }
}