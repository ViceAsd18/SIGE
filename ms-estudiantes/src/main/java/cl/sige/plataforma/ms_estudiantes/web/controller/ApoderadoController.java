package cl.sige.plataforma.ms_estudiantes.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_estudiantes.domain.Apoderado;
import cl.sige.plataforma.ms_estudiantes.domain.ApoderadoEstudiante;
import cl.sige.plataforma.ms_estudiantes.service.ApoderadoEstudianteService;
import cl.sige.plataforma.ms_estudiantes.service.ApoderadoService;
import cl.sige.plataforma.ms_estudiantes.web.dto.apoderado.ApoderadoResponse;
import cl.sige.plataforma.ms_estudiantes.web.dto.apoderado.CrearApoderadoRequest;
import cl.sige.plataforma.ms_estudiantes.web.dto.apoderado_estudiante.ApoderadoEstudianteResponse;
import cl.sige.plataforma.ms_estudiantes.web.dto.relacion.AsociarEstudianteRequest;
import cl.sige.plataforma.ms_estudiantes.web.dto.relacion.EstudianteRelacionResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/apoderados")
@RequiredArgsConstructor
public class ApoderadoController {

    private final ApoderadoService apoderadoService;
    private final ApoderadoEstudianteService apoderadoEstudianteService;


    @PostMapping
    public ResponseEntity<ApoderadoResponse> crear(@RequestBody CrearApoderadoRequest request) {
        Apoderado apoderado = apoderadoService.crear(request.personaRolId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApoderadoResponse(apoderado.getId(), apoderado.getPersonaRolId()));
    }

    @PostMapping("/{apoderadoId}/estudiantes/{estudianteId}")
    public ResponseEntity<ApoderadoEstudianteResponse> asociarEstudiante(
            @PathVariable Long apoderadoId,
            @PathVariable Long estudianteId,
            @RequestBody AsociarEstudianteRequest request) {

        ApoderadoEstudiante relacion = apoderadoEstudianteService.asociar(
                apoderadoId, estudianteId, request.tipoRelacion());

        return ResponseEntity.status(HttpStatus.CREATED).body(new ApoderadoEstudianteResponse(
                relacion.getId(), apoderadoId, estudianteId, relacion.getTipoRelacion()));
    }

    @GetMapping("/{apoderadoId}/estudiantes")
    public ResponseEntity<List<EstudianteRelacionResponse>> obtenerEstudiantes(@PathVariable Long apoderadoId) {
        List<EstudianteRelacionResponse> response = apoderadoEstudianteService
                .obtenerEstudiantesDeApoderado(apoderadoId).stream()
                .map(ae -> new EstudianteRelacionResponse(ae.getEstudiante().getId(), ae.getTipoRelacion()))
                .toList();
        return ResponseEntity.ok(response);
    }


}