package cl.sige.plataforma.ms_estudiantes.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final EstudianteRepository estudianteRepository;
    private final IdentidadAccesoClient identidadAccesoClient;

    @Transactional
    public Estudiante crear(Long personaRolId) {
        if (estudianteRepository.existsByPersonaRolId(personaRolId)) {
            throw new RecursoDuplicadoException("personaRolId", personaRolId);
        }

        validarPersonaRol(personaRolId, "ESTUDIANTE");

        Estudiante estudiante = estudianteRepository.save(new Estudiante(personaRolId));
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