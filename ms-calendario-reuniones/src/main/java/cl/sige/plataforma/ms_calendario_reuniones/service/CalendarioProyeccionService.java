package cl.sige.plataforma.ms_calendario_reuniones.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_calendario_reuniones.domain.CierreAcademicoCalendario;
import cl.sige.plataforma.ms_calendario_reuniones.domain.EvaluacionCalendario;
import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoCierreAcademico;
import cl.sige.plataforma.ms_calendario_reuniones.repository.CierreAcademicoCalendarioRepository;
import cl.sige.plataforma.ms_calendario_reuniones.repository.EvaluacionCalendarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CalendarioProyeccionService {

    private final EvaluacionCalendarioRepository evaluacionRepository;
    private final CierreAcademicoCalendarioRepository cierreRepository;

    /** Idempotente: si el evento llega dos veces, la fila se reemplaza (mismo id). */
    @Transactional
    public void registrarEvaluacion(Long evaluacionId, Long subperiodoId, String nombre, LocalDate fecha) {
        evaluacionRepository.save(new EvaluacionCalendario(evaluacionId, subperiodoId, nombre, fecha));
        log.info("Proyeccion calendario: evaluacion id={} fecha={}", evaluacionId, fecha);
    }

    /** Idempotente: unica fila por (tipo, origenId). */
    @Transactional
    public void registrarCierre(TipoCierreAcademico tipo, Long origenId, String nombre, LocalDate fecha) {
        cierreRepository.findByTipoAndOrigenId(tipo, origenId).ifPresentOrElse(
            existente -> existente.actualizar(nombre, fecha),
            () -> cierreRepository.save(new CierreAcademicoCalendario(tipo, origenId, nombre, fecha)));
        log.info("Proyeccion calendario: cierre {} id={} fecha={}", tipo, origenId, fecha);
    }
}
