package cl.sige.plataforma.ms_academico.web.controller;

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

import cl.sige.plataforma.ms_academico.domain.SubperiodoAcademico;
import cl.sige.plataforma.ms_academico.service.SubperiodoAcademicoService;
import cl.sige.plataforma.ms_academico.web.dto.subperiodo_academico.CrearSubperiodoRequest;
import cl.sige.plataforma.ms_academico.web.dto.subperiodo_academico.SubperiodoAcademicoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/periodos-academicos/{periodoId}/subperiodos")
@RequiredArgsConstructor
public class SubperiodoAcademicoController {
    
    private final SubperiodoAcademicoService subperiodoAcademicoService;

    @PostMapping
    public ResponseEntity<SubperiodoAcademicoResponse> crear(@PathVariable Long periodoId, @RequestBody CrearSubperiodoRequest request) {

        SubperiodoAcademico subperiodo = subperiodoAcademicoService.crear(
            periodoId, request.nombre(), request.fechaInicio(), request.fechaTermino());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(subperiodo));
    }

    @GetMapping
    public ResponseEntity<List<SubperiodoAcademicoResponse>> listar(@PathVariable Long periodoId) {
        
        List<SubperiodoAcademicoResponse> response = subperiodoAcademicoService.obtenerPorPeriodo(periodoId).stream().map(this::aResponse).toList();
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<Void> cerrar(@PathVariable Long periodoId, @PathVariable Long id) {
        subperiodoAcademicoService.cerrar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrir(@PathVariable Long periodoId, @PathVariable Long id) {
        subperiodoAcademicoService.reabrir(id);
        return ResponseEntity.noContent().build();
    }


    private SubperiodoAcademicoResponse aResponse(SubperiodoAcademico s) {
        return new SubperiodoAcademicoResponse(
                s.getId(), s.getPeriodoAcademico().getId(), s.getNombre(),
                s.getFechaInicio(), s.getFechaTermino(), s.getEstado());
    }

}
