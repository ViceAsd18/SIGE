package cl.sige.plataforma.ms_calendario_reuniones.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_calendario_reuniones.domain.EventoInstitucional;
import cl.sige.plataforma.ms_calendario_reuniones.service.EventoInstitucionalService;
import cl.sige.plataforma.ms_calendario_reuniones.web.dto.evento_institucional.CrearEventoInstitucionalRequest;
import cl.sige.plataforma.ms_calendario_reuniones.web.dto.evento_institucional.EventoInstitucionalResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/eventos-institucionales")
public class EventoInstitucionalController {
    
    private final EventoInstitucionalService eventoInstitucionalService;

    @PostMapping
    public ResponseEntity<EventoInstitucionalResponse> crear(@RequestBody CrearEventoInstitucionalRequest request) {
        EventoInstitucional evento = eventoInstitucionalService.crear(
            request.tipo(), request.fecha(), request.descripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new EventoInstitucionalResponse(evento.getId(), evento.getTipo(), evento.getFecha(), evento.getDescripcion()));
    }

    @GetMapping
    public ResponseEntity<List<EventoInstitucionalResponse>> listarEntreFechas(
        @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
            List<EventoInstitucionalResponse> response = eventoInstitucionalService.obtenerEntreFechas(desde, hasta)
                .stream().map(e -> new EventoInstitucionalResponse(e.getId(),e.getTipo(), e.getFecha(), e.getDescripcion()))
                .toList();
            return ResponseEntity.ok(response);
        }



}
