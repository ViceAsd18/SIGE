package cl.sige.plataforma.ms_academico.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.sige.plataforma.ms_academico.domain.PeriodoAcademico;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoPeriodo;
import cl.sige.plataforma.ms_academico.event.AuditoriaEvent;
import cl.sige.plataforma.ms_academico.event.EventPublisher;
import cl.sige.plataforma.ms_academico.event.PeriodoAcademicoCerradoEvent;
import cl.sige.plataforma.ms_academico.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_academico.repository.PeriodoAcademicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeriodoAcademicoService {
    
    private final PeriodoAcademicoRepository periodoAcademicoRepository;

    private final EventPublisher eventPublisher;

    @Transactional
    public PeriodoAcademico crear(String nombreAnio, LocalDate fechaInicio, LocalDate fechaFin) {
        
        if (periodoAcademicoRepository.existsByNombreAnio(nombreAnio)){
            throw new RecursoDuplicadoException("Ya existe un PeriodoAcademico con nombreAnio = " + nombreAnio);    
        }

        PeriodoAcademico periodo = periodoAcademicoRepository.save(
            new PeriodoAcademico(nombreAnio, fechaInicio, fechaFin));
        log.info("PeriodoAcademico creado: id={}, nombreAnio={}", periodo.getId(), nombreAnio);
        return periodo;
    }

    @Transactional(readOnly = true)
    public PeriodoAcademico obtenerPorId(Long id) {
        return periodoAcademicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("PeriodoAcademico", id));   
    }

    @Transactional
    public void cerrarAutomaticamente(PeriodoAcademico periodo, Long personaId, Long personaRolId) {
        if (periodo.getEstado() == EstadoPeriodo.CERRADO) {
            return;
        }

        periodo.cerrar();
        log.info("PeriodoAcademico cerrado Automaticamente: id={}", periodo.getId());

        eventPublisher.publicarPeriodoAcademicoCerrado(new PeriodoAcademicoCerradoEvent(
            periodo.getId(), periodo.getNombreAnio()));

        
        eventPublisher.publicarAuditoria(new AuditoriaEvent(
            personaId, personaRolId, "CERRAR", "PeriodoAcademico", periodo.getId(),
            "ABIERTO", "CERRADO", "Cierre automatico: todos los subperiodos estan cerrados",
            java.time.Instant.now()));

    } 


}
