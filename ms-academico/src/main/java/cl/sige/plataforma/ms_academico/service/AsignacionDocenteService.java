package cl.sige.plataforma.ms_academico.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_academico.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_academico.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_academico.repository.AsignacionDocenteRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.sige.plataforma.ms_academico.domain.AsignacionDocente;
import cl.sige.plataforma.ms_academico.domain.Asignatura;
import cl.sige.plataforma.ms_academico.domain.Curso;
import cl.sige.plataforma.ms_academico.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsignacionDocenteService {
    
    private final AsignacionDocenteRepository asignacionDocenteRepository;
    private final AsignaturaService asignaturaService;
    private final CursoService cursoService;
    private final IdentidadAccesoClient identidadAccesoClient;

    @Transactional
    public AsignacionDocente crear(Long docentePersonaRolId, Long asignaturaId, Long cursoId, LocalDate fechaIncioVigencia) {
        
        Asignatura asignatura = asignaturaService.obtenerPorId(asignaturaId);
        Curso curso = cursoService.obtenerPorId(cursoId);

        PersonaRolClientResponse personaRol;

        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(docentePersonaRolId);
        } catch (FeignException e) {
            throw new RecursoInvalidoException("No existe un PersonaRol con id=" + docentePersonaRolId + " en ms-identidad-acceso");
        }

        if (!"ACTIVO".equals(personaRol.estado()) || !"DOCENTE".equals(personaRol.nombreRol())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + docentePersonaRolId + " no es un Docente activo valido");
        }

        AsignacionDocente asignacion = asignacionDocenteRepository.save(
            new AsignacionDocente(docentePersonaRolId, asignatura, curso, fechaIncioVigencia));
        log.info("AsignacionDocente creada: id={}", asignacion.getId());
        return asignacion;

    }

    @Transactional
    public void finalizarVigencia(Long id, LocalDate fechaTermino) {
        AsignacionDocente asignacion = asignacionDocenteRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("AsignacionDocente", id));
        asignacion.finalizarVigencia(fechaTermino);
        log.info("AsignacionDocente finalizada: id={}", id);
    }

    @Transactional(readOnly = true)
    public AsignacionDocente obtenerPorId(Long id) {
        return asignacionDocenteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("AsignacionDocente", id));
    }

    @Transactional(readOnly = true)
    public List<AsignacionDocente> buscarPorDocente(Long docentePersonaRolId) {
        return asignacionDocenteRepository.findByDocentePersonaRolId(docentePersonaRolId);
    }

}
