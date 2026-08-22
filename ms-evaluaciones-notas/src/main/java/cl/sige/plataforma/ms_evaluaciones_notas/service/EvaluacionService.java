package cl.sige.plataforma.ms_evaluaciones_notas.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_evaluaciones_notas.client.AcademicoClient;
import cl.sige.plataforma.ms_evaluaciones_notas.client.dto.AsignacionDocenteClientResponse;
import cl.sige.plataforma.ms_evaluaciones_notas.client.dto.SubperiodoAcademicoClientResponse;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.Evaluacion;
import cl.sige.plataforma.ms_evaluaciones_notas.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_evaluaciones_notas.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_evaluaciones_notas.repository.EvaluacionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvaluacionService {
    
    private final EvaluacionRepository evaluacionRepository;
    private final AcademicoClient academicoClient;

    @Transactional
    public Evaluacion crear(Long asignacionDocenteId, Long subperiodoAcademicoId, String nombre, LocalDate fecha, Integer ponderacion) {
        
        AsignacionDocenteClientResponse asignacion = validarAsignacionDocente(asignacionDocenteId);
        SubperiodoAcademicoClientResponse subperiodo = obtenerSubperiodo(subperiodoAcademicoId);

        if(!"ABIERTO".equals(subperiodo.estado())) {
            throw new RecursoInvalidoException("El SubperiodoAcademico con id=" + subperiodoAcademicoId + " esta CERRADO, no se pueden crear evaluaciones");
        }

        Evaluacion evaluacion = Evaluacion.builder()
            .asignacionDocenteId(asignacionDocenteId)
            .subperiodoAcademicoId(subperiodoAcademicoId)
            .nombre(nombre)
            .fecha(fecha)
            .ponderacion(ponderacion)
            .build();

        evaluacion = evaluacionRepository.save(evaluacion);
        log.info("Evaluacion creada: id={}, nombre={}", evaluacion.getId(), evaluacion.getNombre());
        return evaluacion;
    }

    @Transactional(readOnly = true)
    public Evaluacion obtenerPorId(Long id) {
        return evaluacionRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Evaluacion", id));
    }

    private AsignacionDocenteClientResponse validarAsignacionDocente(Long id) {
        try {
            AsignacionDocenteClientResponse asignacion = academicoClient.obtenerAsignacionDocente(id);
            
            if(!"VIGENTE".equals(asignacion.estado())) {
                throw new RecursoInvalidoException("La AsignacionDocente con id=" + id + " no esta VIGENTE");
            }
            
            return asignacion;
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe AsignacionDocente con id=" + id + " en ms-academico");
        }
    }

    private SubperiodoAcademicoClientResponse obtenerSubperiodo(Long id) {
        try {
            return academicoClient.obtenerSubperiodo(id);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe SubperiodoAcademico con id=" + id + " en ms-academico");
        }
    }




}
