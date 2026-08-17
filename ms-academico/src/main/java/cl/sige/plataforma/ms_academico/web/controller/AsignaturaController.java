package cl.sige.plataforma.ms_academico.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_academico.domain.Asignatura;
import cl.sige.plataforma.ms_academico.service.AsignaturaService;
import cl.sige.plataforma.ms_academico.web.dto.asignatura.AsignaturaResponse;
import cl.sige.plataforma.ms_academico.web.dto.asignatura.CrearAsignaturaRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/asignaturas")
@RequiredArgsConstructor
public class AsignaturaController {
    
    
    private final AsignaturaService asignaturaService;

    @PostMapping
    public ResponseEntity<AsignaturaResponse> crear(@RequestBody CrearAsignaturaRequest request) {
        
        Asignatura asignatura = asignaturaService.crear(request.nombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(asignatura));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignaturaResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(asignaturaService.obtenerPorId(id)));
    }

    private AsignaturaResponse aResponse(Asignatura asignatura) {
        return new AsignaturaResponse(asignatura.getId(), asignatura.getNombre());
    }


}
