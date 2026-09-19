package cl.sige.plataforma.ms_estudiantes.service;

import cl.sige.plataforma.ms_estudiantes.event.AuditoriaEvent;
import cl.sige.plataforma.ms_estudiantes.event.EstudianteCreadoEvent;
import cl.sige.plataforma.ms_estudiantes.event.EventPublisher;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_estudiantes.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_estudiantes.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_estudiantes.domain.Estudiante;
import cl.sige.plataforma.ms_estudiantes.exception.PersonaRolInvalidoException;
import cl.sige.plataforma.ms_estudiantes.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_estudiantes.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_estudiantes.repository.EstudianteRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class EstudianteService {

    private final EventPublisher eventPublisher;
    private final EstudianteRepository estudianteRepository;
    private final IdentidadAccesoClient identidadAccesoClient;

    @Transactional
    public Estudiante crear(Long personaRolId) {
        if (estudianteRepository.existsByPersonaRolId(personaRolId)) {
            throw new RecursoDuplicadoException("personaRolId", personaRolId);
        }

        validarPersonaRol(personaRolId, "ESTUDIANTE");

        Estudiante estudiante = estudianteRepository.save(new Estudiante(personaRolId));

        eventPublisher.publicarEstudianteCreado(new EstudianteCreadoEvent(
            estudiante.getId(), personaRolId));

        eventPublisher.publicarAuditoria(new AuditoriaEvent(
             personaRolId, personaRolId, "CREAR", "Estudiante", estudiante.getId(),
             null, "personaRolId=" + personaRolId, null, Instant.now()));

        log.info("Estudiante creado: id={}, personaRolId={}", estudiante.getId(), personaRolId);
        return estudiante;
    }

    @Transactional(readOnly = true)
    public Estudiante obtenerPorId(Long id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", id));
    }

    private void validarPersonaRol(Long personaRolId, String rolEsperado) {
        PersonaRolClientResponse personaRol;
        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(personaRolId);
        } catch (FeignException.NotFound e) {
            throw new PersonaRolInvalidoException(
                    "No existe un PersonaRol con id=" + personaRolId + " en ms-identidad-acceso");
        }

        if (!"ACTIVO".equals(personaRol.estado())) {
            throw new PersonaRolInvalidoException(
                    "El PersonaRol id=" + personaRolId + " no esta ACTIVO (estado actual: "
                            + personaRol.estado() + ")");
        }

        if (!rolEsperado.equals(personaRol.nombreRol())) {
            throw new PersonaRolInvalidoException(
                    "El PersonaRol id=" + personaRolId + " tiene rol " + personaRol.nombreRol()
                            + ", se esperaba " + rolEsperado);
        }
    }
}