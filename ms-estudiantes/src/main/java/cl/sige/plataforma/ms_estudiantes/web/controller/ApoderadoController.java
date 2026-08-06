package cl.sige.plataforma.ms_estudiantes.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_estudiantes.domain.Apoderado;
import cl.sige.plataforma.ms_estudiantes.service.ApoderadoService;
import cl.sige.plataforma.ms_estudiantes.web.dto.apoderado.ApoderadoResponse;
import cl.sige.plataforma.ms_estudiantes.web.dto.apoderado.CrearApoderadoRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/apoderados")
@RequiredArgsConstructor
public class ApoderadoController {

    private final ApoderadoService apoderadoService;

    @PostMapping
    public ResponseEntity<ApoderadoResponse> crear(@RequestBody CrearApoderadoRequest request) {
        Apoderado apoderado = apoderadoService.crear(request.personaRolId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApoderadoResponse(apoderado.getId(), apoderado.getPersonaRolId()));
    }

}