package cl.sige.plataforma.ms_academico.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_academico.domain.NivelEducativo;
import cl.sige.plataforma.ms_academico.service.NivelEducativoService;
import cl.sige.plataforma.ms_academico.web.dto.nivel_educativo.CrearNivelEducativoRequest;
import cl.sige.plataforma.ms_academico.web.dto.nivel_educativo.NivelEducativoResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/niveles-educativos")
@RequiredArgsConstructor
public class NivelEducativoController {
    
    private final NivelEducativoService nivelEducativoService;

    @PostMapping
    public ResponseEntity<NivelEducativoResponse> crear(@RequestBody CrearNivelEducativoRequest request ) {
        
        NivelEducativo nivel = nivelEducativoService.crear(request.nombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(nivel)); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelEducativoResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(aResponse(nivelEducativoService.obtenerPorId(id)));
    }


    private NivelEducativoResponse aResponse(NivelEducativo nivel) {
        return new NivelEducativoResponse(nivel.getId(), nivel.getNombre());
    }


}
