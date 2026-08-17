package cl.sige.plataforma.ms_academico.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_academico.domain.PeriodoAcademico;
import cl.sige.plataforma.ms_academico.service.PeriodoAcademicoService;
import cl.sige.plataforma.ms_academico.web.dto.periodo_academico.CrearPeriodoAcademicoRequest;
import cl.sige.plataforma.ms_academico.web.dto.periodo_academico.PeriodoAcademicoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/periodos-academicos")
@RequiredArgsConstructor
public class PeriodoAcademicoController {
    
    private final PeriodoAcademicoService periodoAcademicoService;

    @PostMapping
    public ResponseEntity<PeriodoAcademicoResponse> crear(@RequestBody CrearPeriodoAcademicoRequest request) {
        
        PeriodoAcademico periodo = periodoAcademicoService.crear(
            request.nombreAnio(), request.fechaInicio(), request.fechaTermino());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(periodo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoAcademicoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(periodoAcademicoService.obtenerPorId(id)));
    }

    @PatchMapping("/{id}/cerrar")
    public ResponseEntity<Void> cerrar(@PathVariable Long id) {
        periodoAcademicoService.cerrar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<Void> reabrir(@PathVariable Long id) {
        periodoAcademicoService.reabrir(id);
        return ResponseEntity.noContent().build();
    }


    private PeriodoAcademicoResponse aResponse(PeriodoAcademico periodo) {
        return new PeriodoAcademicoResponse(
                periodo.getId(), periodo.getNombreAnio(), periodo.getFechaInicio(),
                periodo.getFechaTermino(), periodo.getEstado());
    }



}
