package cl.sige.plataforma.ms_evaluaciones_notas.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import cl.sige.plataforma.ms_evaluaciones_notas.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_evaluaciones_notas.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.Calificacion;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.SolicitudExcepcionCalificacion;
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

    @Transactional
    public SolicitudExcepcionCalificacion crear(Long calificacionId, Long solicitantePersonaRolId, String motivo) {
        Calificacion calificacion = calificacionRepository.findByIdConEvaluacion(calificacionId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Calificacion", calificacionId));
    
        validarRol(solicitantePersonaRolId, "DOCENTE");

        SolicitudExcepcionCalificacion solicitud = solicitudRepository.save(
            new SolicitudExcepcionCalificacion(calificacion, solicitantePersonaRolId, motivo));
        log.info("SolicitudExceptionCalificacion creada: id={} calificacionId={}", solicitud.getId(), calificacionId);
        return solicitud;

    } 

    @Transactional
    public SolicitudExcepcionCalificacion aprobar(Long solicitudId, Long aprobadorPersonaRolId, BigDecimal nuevoResultado) {
        SolicitudExcepcionCalificacion solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RecursoNoEncontradoException("SolicitudExcepcionCalificacion", solicitudId));

        validarRol(aprobadorPersonaRolId, "DIRECTIVO");

        solicitud.aprobar(aprobadorPersonaRolId);
        //Ejecuta la modificacion directamente sobre la calificacion, sin pasar por CalificacionService.modificar() por que ese metodo exige el
        //Subperiodo ABIERTO - la excepcion existe justamente para el caso contrario.
        solicitud.getCalificacion().modificar(nuevoResultado);
        solicitud.marcarEjecutada();

        log.info("SolicitudExceptionCalificacion aprobada y ejecutada: id={}", solicitudId);
        return solicitud;

    }

    @Transactional
    public SolicitudExcepcionCalificacion rechazar(Long solicitudId, Long aprobadorPersonaRolId) {
        SolicitudExcepcionCalificacion solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RecursoNoEncontradoException("SolicitudExcepcionCalificacion", solicitudId));

        validarRol(aprobadorPersonaRolId, "DIRECTIVO");

        solicitud.rechazar(aprobadorPersonaRolId);

        log.info("SolicitudExceptionCalificacion rechazada: id={}", solicitudId);
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
