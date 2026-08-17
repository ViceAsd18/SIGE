package cl.sige.plataforma.ms_academico.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_academico.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_academico.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_academico.repository.CursoRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.sige.plataforma.ms_academico.domain.Curso;
import cl.sige.plataforma.ms_academico.domain.NivelEducativo;
import cl.sige.plataforma.ms_academico.domain.PeriodoAcademico;
import cl.sige.plataforma.ms_academico.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_academico.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CursoService {
    
    private final CursoRepository cursoRepository;
    private final NivelEducativoService nivelEducativoService;
    private final PeriodoAcademicoService periodoAcademicoService;
    private final IdentidadAccesoClient identidadAccesoClient;

    @Transactional
    public Curso crear(Long nivelEducativoId, Long periodoAcademicoId, String paralelo, Long profesorJefePersonaRolId) {
        
        NivelEducativo nivel = nivelEducativoService.obtenerPorId(nivelEducativoId);
        PeriodoAcademico periodo = periodoAcademicoService.obtenerPorId(periodoAcademicoId);

        if(cursoRepository.existsByNivelEducativoIdAndPeriodoAcademicoIdAndParalelo(nivelEducativoId, periodoAcademicoId, paralelo)) {
            throw new RecursoDuplicadoException("Ya existe un Curso con ese nivel, periodo y paralelo (" + paralelo + ")");
        }

        validarDocente(profesorJefePersonaRolId);

        Curso curso = Curso.builder()
            .nivelEducativo(nivel)
            .periodoAcademico(periodo)
            .paralelo(paralelo)
            .profesorJefePersonaRolId(profesorJefePersonaRolId)
            .build();

        curso = cursoRepository.save(curso);
        log.info("Curso creado con id={}, paralelo={}", curso.getId(), paralelo);
        return curso;

    }

    @Transactional(readOnly = true)
    public Curso obtenerPorId(Long id) {
        return cursoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Curso", id));
    }

    private void validarDocente(Long personaRolId) {

        PersonaRolClientResponse personaRol;

        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(personaRolId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe un PersonaRol con id=" + personaRolId + " en ms-identidad-acceso");
        }

        if(!"ACTIVO".equals(personaRol.estado())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + personaRolId + " no esta ACTIVO");
        }

        if(!"DOCENTE".equals(personaRol.nombreRol())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + personaRolId + " tiene rol " + personaRol.nombreRol() + ", se esperaba DOCENTE");
        }


    }


}
