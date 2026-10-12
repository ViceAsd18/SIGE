package cl.sige.plataforma.ms_calendario_reuniones.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoCierreAcademico;
import cl.sige.plataforma.ms_calendario_reuniones.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_calendario_reuniones.repository.CierreAcademicoCalendarioRepository;
import cl.sige.plataforma.ms_calendario_reuniones.repository.EventoInstitucionalRepository;
import cl.sige.plataforma.ms_calendario_reuniones.repository.EvaluacionCalendarioRepository;
import cl.sige.plataforma.ms_calendario_reuniones.repository.ReunionRepository;
import cl.sige.plataforma.ms_calendario_reuniones.web.dto.calendario.CalendarioItemResponse;
import cl.sige.plataforma.ms_calendario_reuniones.web.dto.calendario.TipoItemCalendario;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarioService {

    private static final long MAX_DIAS_RANGO = 366;

    private final ReunionRepository reunionRepository;
    private final EventoInstitucionalRepository eventoRepository;
    private final EvaluacionCalendarioRepository evaluacionRepository;
    private final CierreAcademicoCalendarioRepository cierreRepository;

    @Transactional(readOnly = true)
    public List<CalendarioItemResponse> obtener(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) {
            throw new RecursoInvalidoException("Los parametros 'desde' y 'hasta' son obligatorios");
        }
        if (hasta.isBefore(desde)) {
            throw new RecursoInvalidoException("'hasta' no puede ser anterior a 'desde'");
        }
        if (ChronoUnit.DAYS.between(desde, hasta) > MAX_DIAS_RANGO) {
            throw new RecursoInvalidoException("El rango maximo es de " + MAX_DIAS_RANGO + " dias");
        }

        List<CalendarioItemResponse> items = new ArrayList<>();

        reunionRepository.findByFechaBetween(desde, hasta).forEach(r -> items.add(new CalendarioItemResponse(
            TipoItemCalendario.REUNION, r.getId(), "Reunion: " + r.getTipo(),
            r.getTipo().name(), r.getEstado().name(),
            r.getFecha(), r.getHoraInicio(), r.getHoraTermino())));

        eventoRepository.findByFechaBetween(desde, hasta).forEach(e -> items.add(new CalendarioItemResponse(
            TipoItemCalendario.EVENTO_INSTITUCIONAL, e.getId(), e.getDescripcion(),
            e.getTipo().name(), null, e.getFecha(), null, null)));

        evaluacionRepository.findByFechaBetween(desde, hasta).forEach(ev -> items.add(new CalendarioItemResponse(
            TipoItemCalendario.EVALUACION, ev.getId(), ev.getNombre(),
            null, null, ev.getFecha(), null, null)));

        cierreRepository.findByFechaBetween(desde, hasta).forEach(c -> {
            boolean esSubperiodo = c.getTipo() == TipoCierreAcademico.SUBPERIODO;
            items.add(new CalendarioItemResponse(
                esSubperiodo ? TipoItemCalendario.CIERRE_SUBPERIODO : TipoItemCalendario.CIERRE_PERIODO,
                c.getOrigenId(),
                (esSubperiodo ? "Cierre de subperiodo: " : "Cierre de periodo: ") + c.getNombre(),
                null, null, c.getFecha(), null, null));
        });

        items.sort(Comparator
            .comparing(CalendarioItemResponse::fecha)
            .thenComparing(CalendarioItemResponse::horaInicio, Comparator.nullsFirst(Comparator.naturalOrder()))
            .thenComparing(CalendarioItemResponse::tipo));
        return items;
    }
}
