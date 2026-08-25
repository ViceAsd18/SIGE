package cl.sige.plataforma.ms_mensajeria.service;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_mensajeria.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_mensajeria.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_mensajeria.domain.Conversacion;
import cl.sige.plataforma.ms_mensajeria.domain.Mensaje;
import cl.sige.plataforma.ms_mensajeria.domain.SolicitudAccesoConversacion;
import cl.sige.plataforma.ms_mensajeria.domain.enums.EstadoSolicitud;
import cl.sige.plataforma.ms_mensajeria.exception.AccesoNoAutorizadoException;
import cl.sige.plataforma.ms_mensajeria.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_mensajeria.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_mensajeria.repository.SolicitudAccesoConversacionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudAccesoConversacionService {
    
    private static final Set<String> ROLES_SOLICITANTES = Set.of("INSPECTOR", "DIRECTIVO");

    private final SolicitudAccesoConversacionRepository solicitudRepository;
    private final ConversacionService conversacionService;
    private final IdentidadAccesoClient identidadAccesoClient;
    private final MensajeService mensajeService;


    @Transactional
    public SolicitudAccesoConversacion crear(Long conversacionId, Long solicitantePersonaRolId, String motivo) {
        Conversacion conversacion = conversacionService.obtenerPorId(conversacionId);
        validarRol(solicitantePersonaRolId, ROLES_SOLICITANTES);

        SolicitudAccesoConversacion solicitud = solicitudRepository.save(
            new SolicitudAccesoConversacion(conversacion, solicitantePersonaRolId, motivo));
        log.info("SolicitudAccesoConversacion creada: id={}, conversacionId={}",  solicitud.getId(), conversacionId);
        return solicitud;

    }

    @Transactional
    public SolicitudAccesoConversacion aprobar(Long solicitudId, Long aprobadorPersonaRolId) {
        SolicitudAccesoConversacion solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RecursoNoEncontradoException("SolicitudAccesoConversacion", aprobadorPersonaRolId));

        validarRol(aprobadorPersonaRolId, Set.of("DIRECTIVO"));
        
        solicitud.aprobar(aprobadorPersonaRolId);
        solicitud.marcarEjecutada();
        log.info("SolicitudAccesoConversacion aprobada y ejecutada: id={}", solicitudId);
        return solicitud;

    }

    @Transactional
    public SolicitudAccesoConversacion rechazar(Long solicitudId, Long aprobadorPersonaRolId) {
        SolicitudAccesoConversacion solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new RecursoNoEncontradoException("SolicitudAccesoConversacion", aprobadorPersonaRolId));

        validarRol(aprobadorPersonaRolId, Set.of("DIRECTIVO"));
        solicitud.rechazar(aprobadorPersonaRolId);
        log.info("SolicitudAccesoConversacion rechazada: id={}", solicitudId);
        return solicitud;

    }



    private void validarRol(Long personaRolId, Set<String> rolesPermitidos) {
        PersonaRolClientResponse personaRol;
        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(personaRolId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe PersonaRol con id=" + personaRolId);
        }
        if(!"ACTIVO".equals(personaRol.estado()) || !rolesPermitidos.contains(personaRol.nombreRol())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + personaRolId + " no tiene un rol autorizado (" + rolesPermitidos + ")");
        }

    }



    @Transactional(readOnly = true)
    public List<Mensaje> obtenerMensajesConAcceso(Long solicitudId) {
        SolicitudAccesoConversacion solicitud = solicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RecursoNoEncontradoException("SolicitudAccesoConversacion", solicitudId));

        if (solicitud.getEstado() != EstadoSolicitud.EJECUTADA) {
            throw new AccesoNoAutorizadoException(
                    "La solicitud id=" + solicitudId + " no ha sido aprobada/ejecutada, no se puede acceder al contenido");
        }

        return mensajeService.obtenerPorConversacion(solicitud.getConversacion().getId());
    }


}
