package cl.sige.plataforma.ms_academico.web.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_academico.service.SubperiodoAcademicoService;
import cl.sige.plataforma.ms_academico.web.dto.subperiodo_academico.SubperiodoAcademicoResponse;
import lombok.RequiredArgsConstructor;

import cl.sige.plataforma.ms_academico.domain.SubperiodoAcademico;

@RestController
@RequestMapping("/subperiodos-academicos")
@RequiredArgsConstructor
public class SubperiodoAcademicoDirectoController {
    
    private final SubperiodoAcademicoService subperiodoAcademicoService;

    @GetMapping("/{id}")
    public ResponseEntity<SubperiodoAcademicoResponse> obtener(@PathVariable Long id) {
        SubperiodoAcademico subperiodo = subperiodoAcademicoService.obtenerPorId(id);
        return ResponseEntity.ok(new SubperiodoAcademicoResponse(
            subperiodo.getId(), 
            subperiodo.getPeriodoAcademico().getId(), 
            subperiodo.getNombre(),
            subperiodo.getFechaInicio(), 
            subperiodo.getFechaTermino(), 
            subperiodo.getEstado()
        ));
    }

}
