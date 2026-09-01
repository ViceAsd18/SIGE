package cl.sige.plataforma.ms_calendario_reuniones.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_calendario_reuniones.client.AcademicoClient;
import cl.sige.plataforma.ms_calendario_reuniones.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_calendario_reuniones.client.dto.CursoClientResponse;
import cl.sige.plataforma.ms_calendario_reuniones.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_calendario_reuniones.domain.Reunion;
import cl.sige.plataforma.ms_calendario_reuniones.domain.ReunionParticipante;
import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoReunion;
import cl.sige.plataforma.ms_calendario_reuniones.exception.AccesoNoAutorizadoException;
import cl.sige.plataforma.ms_calendario_reuniones.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_calendario_reuniones.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_calendario_reuniones.repository.ReunionParticipanteRepository;
import cl.sige.plataforma.ms_calendario_reuniones.repository.ReunionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReunionService {
    
    private final ReunionRepository reunionRepository;
    private final ReunionParticipanteRepository participanteRepository;
    private final IdentidadAccesoClient identidadAccesoClient;
    private final AcademicoClient academicoClient;

    @Transactional
    public Reunion crear(TipoReunion tipo, LocalDate fecha, LocalTime horaInicio, LocalTime horaTermino, 
        Long convocantePersonaRolId, String lugarModalidad, Long cursoId, List<Long> participantePersonaIds) {
        
        PersonaRolClientResponse convocante = obtenerPersonaRol(convocantePersonaRolId);
        validarConvocante(tipo, convocante, cursoId);
        
        Reunion reunion = reunionRepository.save(
            new Reunion(tipo, fecha, horaInicio, horaTermino, convocantePersonaRolId, lugarModalidad));

        for(Long personaId : participantePersonaIds) {
            participanteRepository.save(new ReunionParticipante(reunion, personaId));
        }

        log.info("Reunion creada: id={}, tipo={}, convocantePersonaRolId={}", reunion.getId(), tipo, convocantePersonaRolId);
        return reunion;

    }

    @Transactional
    public void marcarReliazada(Long id) {
        Reunion reunion = obtenerPorId(id);
        reunion.marcarRealizada();
        log.info("Reunion marcada como realizada: id={}", id);
    }

    @Transactional
    public void cancelar(Long id) {
        Reunion reunion = obtenerPorId(id);
        reunion.cancelar();
        log.info("Reunion Cancelada: id={}", id);
    }



    @Transactional(readOnly = true)
    public Reunion obtenerPorId(Long id) {
        return reunionRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Reunion", id));
    }

    private PersonaRolClientResponse obtenerPersonaRol(Long personaRolId) {
        try {
            return identidadAccesoClient.obtenerPersonaRol(personaRolId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe PersonaRol con id=" + personaRolId);
        }
    }
    
    @Transactional(readOnly = true)
    public List<ReunionParticipante> obtenerParticipantes(Long reunionId) {
        return participanteRepository.findByReunionId(reunionId);
    }

    @Transactional(readOnly = true)
    public List<Reunion> obtenerEntreFechas(LocalDate desde, LocalDate hasta) {
        return reunionRepository.findByFechaBetween(desde, hasta);
    }




    
    private void validarConvocante(TipoReunion tipo, PersonaRolClientResponse convocante, Long cursoId) {
        if (!"ACTIVO".equals(convocante.estado())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + convocante.id() + " no esta ACTIVO");
        }

        switch (tipo) {
            case CURSO -> {
                if (cursoId == null) {
                    throw new RecursoInvalidoException("Reunion de tipo CURSO requiere cursoId");
                }
                if (!"DOCENTE".equals(convocante.nombreRol())) {
                    throw new AccesoNoAutorizadoException("Solo un Docente (Profesor Jefe) puede convocar reunion de CURSO");
                }
                CursoClientResponse curso;
                try {
                    curso = academicoClient.obtenerCurso(cursoId);
                } catch (FeignException.NotFound e) {
                    throw new RecursoInvalidoException("No existe Curso con id=" + cursoId);
                }
                if (!curso.profesorJefePersonaRolId().equals(convocante.id())) {
                    throw new AccesoNoAutorizadoException(
                            "El convocante no es el Profesor Jefe del curso id=" + cursoId);
                }
            }
            case APODERADO_DOCENTE -> {
                if (!"DOCENTE".equals(convocante.nombreRol())) {
                    throw new AccesoNoAutorizadoException("Solo un Docente puede convocar reunion APODERADO_DOCENTE");
                }
            }
            case CONVIVENCIA_DISCIPLINARIA -> {
                if (!"INSPECTOR".equals(convocante.nombreRol())) {
                    throw new AccesoNoAutorizadoException("Solo un Inspector puede convocar reunion de CONVIVENCIA_DISCIPLINARIA");
                }
            }
            case INSTITUCIONAL, CONSEJO_PROFESORES -> {
                if (!"DIRECTIVO".equals(convocante.nombreRol())) {
                    throw new AccesoNoAutorizadoException("Solo un Directivo puede convocar reunion " + tipo);
                }
            }
        }
    }


}
