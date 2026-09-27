package cl.sige.plataforma.ms_evaluaciones_notas.service;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.stereotype.Service;

import cl.sige.plataforma.ms_evaluaciones_notas.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_evaluaciones_notas.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.Calificacion;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.SolicitudExcepcionCalificacion;
import cl.sige.plataforma.ms_evaluaciones_notas.event.AuditoriaEvent;
import cl.sige.plataforma.ms_evaluaciones_notas.event.EventPublisher;
import cl.sige.plataforma.ms_evaluaciones_notas.event.SolicitudExcepcionEvent;
import cl.sige.plataforma.ms_evaluaciones_notas.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_evaluaciones_notas.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_evaluaciones_notas.repository.CalificacionRepository;
import cl.sige.plataforma.ms_evaluaciones_notas.repository.SolicitudExcepcionCalificacionRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudExcepcionCalificacionService {
    
    private final SolicitudExcepcionCalificacionRepository solicitudRepository;
    private final CalificacionRepository calificacionRepository;
    private final CalificacionService calificacionService;
    private final IdentidadAccesoClient identidadAccesoClient;

    private final EventPublisher eventPublisher;


    private static final String ENTIDAD_TIPO = "SolicitudExcepcionCalificacion";

    @Transactional
    public SolicitudExcepcionCalificacion crear(Long calificacionId, Long solicitantePersonaRolId, String motivo, BigDecimal nuevoResultado) {
        Calificacion calificacion = calificacionRepository.findByIdConEvaluacion(calificacionId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Calificacion", calificacionId));
    
        validarRol(solicitantePersonaRolId, "DOCENTE");

        SolicitudExcepcionCalificacion solicitud = solicitudRepository.save(
            new SolicitudExcepcionCalificacion(calificacion, solicitantePersonaRolId, motivo, nuevoResultado));
        log.info("SolicitudExceptionCalificacion creada: id={} calificacionId={} nuevoResultado={}",
        solicitud.getId(), calificacionId, nuevoResultado);

        eventPublisher.publicarSolicitudExcepcion(new SolicitudExcepcionEvent(
            solicitud.getId(), calificacionId, solicitantePersonaRolId, null, "PENDIENTE"
        ));

        eventPublisher.publicarAuditoria(new AuditoriaEvent(
            solicitantePersonaRolId, solicitantePersonaRolId, "CREAR", ENTIDAD_TIPO, solicitud.getId(),
            null, motivo, null, java.time.Instant.now()));
        
        return solicitud;

    } 

    @Transactional
    public SolicitudExcepcionCalificacion aprobar(Long solicitudId, Long aprobadorPersonaRolId) {
        SolicitudExcepcionCalificacion solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RecursoNoEncontradoException("SolicitudExceptionCalificacion", solicitudId));

        validarRol(aprobadorPersonaRolId, "DIRECTIVO");

        solicitud.aprobar(aprobadorPersonaRolId);
        solicitud.getCalificacion().modificar(solicitud.getNuevoResultado());
        solicitud.marcarEjecutada();

        log.info("SolicitudExceptionCalificacion aprobada y ejecutada: id={}", solicitudId);

        eventPublisher.publicarSolicitudExcepcion(new SolicitudExcepcionEvent(
            solicitudId, solicitud.getCalificacion().getId(), solicitud.getSolicitantePersonaRolId(),
            aprobadorPersonaRolId, "APROBADA"));

        eventPublisher.publicarAuditoria(new AuditoriaEvent(
            aprobadorPersonaRolId, aprobadorPersonaRolId, "APROBAR", ENTIDAD_TIPO, solicitudId,
            "PENDIENTE", "APROBADA", null, Instant.now()));

        return solicitud;

    }

    @Transactional
    public SolicitudExcepcionCalificacion rechazar(Long solicitudId, Long aprobadorPersonaRolId) {
        SolicitudExcepcionCalificacion solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RecursoNoEncontradoException("SolicitudExceptionCalificacion", solicitudId));

        validarRol(aprobadorPersonaRolId, "DIRECTIVO");

        solicitud.rechazar(aprobadorPersonaRolId);

        log.info("SolicitudExceptionCalificacion rechazada: id={}", solicitudId);

        eventPublisher.publicarSolicitudExcepcion(new SolicitudExcepcionEvent(
            solicitudId, solicitud.getCalificacion().getId(), solicitud.getSolicitantePersonaRolId(),
            aprobadorPersonaRolId, "RECHAZADA"));

        eventPublisher.publicarAuditoria(new AuditoriaEvent(
            aprobadorPersonaRolId, aprobadorPersonaRolId, "RECHAZAR", ENTIDAD_TIPO, solicitudId,
            "PENDIENTE", "RECHAZADA", null, Instant.now()));

        return solicitud;

    }


    private void validarRol(Long personaRolId, String rolEsperado) {
        PersonaRolClientResponse personaRol;

        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(personaRolId);            
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe PersonaRol con id=" + personaRolId);
        }

        if(!"ACTIVO".equals(personaRol.estado()) || !rolEsperado.equals(personaRol.nombreRol())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + personaRolId + " no es un " + rolEsperado + " activo valido");
        }

    }



}
