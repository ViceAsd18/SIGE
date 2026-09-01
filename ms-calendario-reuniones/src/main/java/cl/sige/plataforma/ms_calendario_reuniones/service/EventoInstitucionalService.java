package cl.sige.plataforma.ms_calendario_reuniones.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_calendario_reuniones.repository.EventoInstitucionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.sige.plataforma.ms_calendario_reuniones.domain.EventoInstitucional;
import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoEventoInstitucional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventoInstitucionalService {
    
    private final EventoInstitucionalRepository eventoInstitucionalRepository;


    @Transactional
    public EventoInstitucional crear(TipoEventoInstitucional tipo, LocalDate fecha, String descripcion) {
        EventoInstitucional evento = EventoInstitucional.builder()
            .tipo(tipo)
            .fecha(fecha)
            .descripcion(descripcion)
            .build();
        evento = eventoInstitucionalRepository.save(evento);
        log.info("EventoInstitucional creado: id={}, tipo={}", evento.getId(), tipo);
        return evento;
    }

    @Transactional(readOnly = true)
    public List<EventoInstitucional> obtenerEntreFechas(LocalDate desde, LocalDate hasta) {
        return eventoInstitucionalRepository.findByFechaBetween(desde, hasta);
    }

}
