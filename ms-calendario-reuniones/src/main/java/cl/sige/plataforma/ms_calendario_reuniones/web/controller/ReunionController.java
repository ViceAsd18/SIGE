package cl.sige.plataforma.ms_calendario_reuniones.web.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sige.plataforma.ms_calendario_reuniones.domain.Reunion;
import cl.sige.plataforma.ms_calendario_reuniones.service.ReunionService;
import cl.sige.plataforma.ms_calendario_reuniones.web.dto.reunion.CrearReunionRequest;
import cl.sige.plataforma.ms_calendario_reuniones.web.dto.reunion.ReunionResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/reuniones")
@RequiredArgsConstructor
public class ReunionController {
 
    private final ReunionService reunionService;

    @PostMapping
    public ResponseEntity<ReunionResponse> crear(@RequestBody CrearReunionRequest request) {
        Reunion reunion = reunionService.crear(request.tipo(), request.fecha(), request.horaInicio(), request.horaTermino(),
                request.convocantePersonaRolId(), request.lugarModalidad(), request.cursoId(), request.participantesPersonaIds());

        return ResponseEntity.status(HttpStatus.CREATED).body(aResponse(reunion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReunionResponse> obtener(@PathVariable("id") Long id) {
        return ResponseEntity.ok(aResponse(reunionService.obtenerPorId(id)));
    }

    @PatchMapping("/{id}/realizar")
    public ResponseEntity<Void> marcarRealizada(@PathVariable("id") Long id) {
        reunionService.marcarReliazada(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelar(@PathVariable("id") Long id) {
        reunionService.cancelar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ReunionResponse>> listarEntreFechas(
        @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {
        
        List<ReunionResponse> response = reunionService.obtenerEntreFechas(desde, hasta).stream().map(this::aResponse).toList();
            return ResponseEntity.ok(response);
    }


    private ReunionResponse aResponse(Reunion r) {
        return new ReunionResponse(r.getId(), r.getTipo(), r.getFecha(), r.getHoraInicio(),
                r.getHoraTermino(), r.getConvocantePersonaRolId(), r.getEstado(), r.getLugarModalidad());
    }

}
