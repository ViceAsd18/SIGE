package cl.sige.plataforma.ms_calendario_reuniones.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_calendario_reuniones.service.CalendarioService;
import cl.sige.plataforma.ms_calendario_reuniones.web.dto.calendario.CalendarioItemResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calendario")
@RequiredArgsConstructor
public class CalendarioController {

    private final CalendarioService calendarioService;

    @GetMapping
    public ResponseEntity<List<CalendarioItemResponse>> obtener(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        return ResponseEntity.ok(calendarioService.obtener(desde, hasta));
    }
}