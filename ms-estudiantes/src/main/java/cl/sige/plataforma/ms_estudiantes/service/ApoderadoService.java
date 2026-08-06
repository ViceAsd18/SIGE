package cl.sige.plataforma.ms_estudiantes.service;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_estudiantes.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_estudiantes.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_estudiantes.domain.Apoderado;
import cl.sige.plataforma.ms_estudiantes.exception.PersonaRolInvalidoException;
import cl.sige.plataforma.ms_estudiantes.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_estudiantes.repository.ApoderadoRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApoderadoService {

    private final ApoderadoRepository apoderadoRepository;
    private final IdentidadAccesoClient identidadAccesoClient;

    @Transactional
    public Apoderado crear(Long personaRolId) {
        
        if (apoderadoRepository.existsByPersonaRolId(personaRolId)) {
            throw new RecursoDuplicadoException("personaRolId", personaRolId);
        }

        PersonaRolClientResponse personaRol;

        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(personaRolId);
        } catch (FeignException.NotFound e) {
            throw new PersonaRolInvalidoException(
                    "No existe un PersonaRol con id=" + personaRolId + " en ms-identidad-acceso");
        }

        if (!"ACTIVO".equals(personaRol.estado())) {
            throw new PersonaRolInvalidoException("El PersonaRol id=" + personaRolId + " no esta ACTIVO");
        }
        if (!"APODERADO".equals(personaRol.nombreRol())) {
            throw new PersonaRolInvalidoException(
                    "El PersonaRol id=" + personaRolId + " tiene rol " + personaRol.nombreRol()
                            + ", se esperaba APODERADO");
        }

        Apoderado apoderado = apoderadoRepository.save(new Apoderado(personaRolId));
        log.info("Apoderado creado: id={}, personaRolId={}", apoderado.getId(), personaRolId);
        return apoderado;
    }
}