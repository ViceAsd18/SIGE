package cl.sige.plataforma.ms_academico.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_academico.domain.Curso;
import cl.sige.plataforma.ms_academico.service.CursoService;
import cl.sige.plataforma.ms_academico.web.dto.curso.CrearCursoRequest;
import cl.sige.plataforma.ms_academico.web.dto.curso.CursoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cursos")
@RequiredArgsConstructor
public class CursoController {
    
    private final CursoService cursoService;

    @PostMapping
    public ResponseEntity<CursoResponse> crear(@RequestBody CrearCursoRequest request) {
        Curso curso = cursoService.crear(
            request.nivelEducativoId(), request.periodoAcademicoId(), 
            request.paralelo(), request.profesorJefePersonaRolId());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(curso));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(cursoService.obtenerPorId(id)));
    }

    private CursoResponse aResponse(Curso curso) {
        return new CursoResponse(
                curso.getId(), curso.getNivelEducativo().getId(), curso.getPeriodoAcademico().getId(),
                curso.getParalelo(), curso.getProfesorJefePersonaRolId());
    }


}
