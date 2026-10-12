package cl.sige.plataforma.ms_academico.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_academico.repository.SubperiodoAcademicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.sige.plataforma.ms_academico.domain.SubperiodoAcademico;
import cl.sige.plataforma.ms_academico.event.AuditoriaEvent;
import cl.sige.plataforma.ms_academico.event.EventPublisher;
import cl.sige.plataforma.ms_academico.event.SubperiodoAcademicoCerradoEvent;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_academico.domain.PeriodoAcademico;

import cl.sige.plataforma.ms_academico.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_academico.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoPeriodo;
import cl.sige.plataforma.ms_academico.exception.RecursoInvalidoException;
import feign.FeignException;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubperiodoAcademicoService {
    
    private final SubperiodoAcademicoRepository subperiodoAcademicoRepository;
    private final PeriodoAcademicoService periodoAcademicoService;
    private final IdentidadAccesoClient identidadAccesoClient;

    private final EventPublisher eventPublisher;

    @Transactional
    public SubperiodoAcademico crear(Long periodoAcademicoId, String nombre, LocalDate fechaInicio, LocalDate fechaTermino) {

        PeriodoAcademico periodo = periodoAcademicoService.obtenerPorId(periodoAcademicoId);

        SubperiodoAcademico subperiodo = subperiodoAcademicoRepository.save(
            new SubperiodoAcademico(periodo, nombre, fechaInicio, fechaTermino));
        log.info("SubperiodoAcademico creado: id={}, periodoId={}", subperiodo.getId(), periodoAcademicoId);
        return subperiodo;
    }

    @Transactional(readOnly = true)
    public List<SubperiodoAcademico> obtenerPorPeriodo(Long periodoAcademicoId) {
        return subperiodoAcademicoRepository.findByPeriodoAcademicoId(periodoAcademicoId);
    }

    @Transactional
    public void cerrar(Long id, Long cerradorPersonaRolId) {
        PersonaRolClientResponse directivo = validarDirectivo(cerradorPersonaRolId);

        SubperiodoAcademico subperiodo = subperiodoAcademicoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("SubperiodoAcademico", id));

        if (subperiodo.getEstado() == EstadoPeriodo.CERRADO) {
            throw new RecursoInvalidoException(
                "El subperiodo id=" + id + " ya esta CERRADO. El cierre es definitivo.");
        }

        subperiodo.cerrar();
        PeriodoAcademico periodo = subperiodo.getPeriodoAcademico();
        log.info("SubperiodoAcademico cerrado: id={}, periodoId={}", id, periodo.getId());

        eventPublisher.publicarSubperiodoAcademicoCerrado(new SubperiodoAcademicoCerradoEvent(
            subperiodo.getId(), periodo.getId(), subperiodo.getNombre(), subperiodo.getFechaTermino()));

        eventPublisher.publicarAuditoria(new AuditoriaEvent(
            directivo.personaId(), cerradorPersonaRolId, "CERRAR", "SubperiodoAcademico", id,
            "ABIERTO", "CERRADO", null, java.time.Instant.now()));

        boolean todosCerrados = subperiodoAcademicoRepository.findByPeriodoAcademicoId(periodo.getId())
            .stream().allMatch(s -> s.getEstado() == EstadoPeriodo.CERRADO);
        if (todosCerrados) {
            periodoAcademicoService.cerrarAutomaticamente(periodo, directivo.personaId(), cerradorPersonaRolId);
        }
    }


    @Transactional(readOnly = true)
    public SubperiodoAcademico obtenerPorId(Long id) {
        return subperiodoAcademicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("SubperiodoAcademico", id));
    }

    private PersonaRolClientResponse validarDirectivo(Long personaRolId) {
        if (personaRolId == null) {
            throw new RecursoInvalidoException("cerradorPersonaRolId es obligatorio");
        }
        PersonaRolClientResponse pr;
        try {
            pr = identidadAccesoClient.obtenerPersonaRol(personaRolId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe un PersonaRol con id=" + personaRolId + " en ms-identidad-acceso");
        }
        if (!"ACTIVO".equals(pr.estado())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + personaRolId + " no esta ACTIVO");
        }
        if (!"DIRECTIVO".equals(pr.nombreRol())) {
            throw new RecursoInvalidoException("Solo un DIRECTIVO puede cerrar un subperiodo (rol actual: " + pr.nombreRol() + ")");
        }
        return pr;
    }

}
