package cl.sige.plataforma.ms_evaluaciones_notas.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_evaluaciones_notas.client.AcademicoClient;
import cl.sige.plataforma.ms_evaluaciones_notas.client.dto.SubperiodoAcademicoClientResponse;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.Calificacion;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.Evaluacion;
import cl.sige.plataforma.ms_evaluaciones_notas.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_evaluaciones_notas.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_evaluaciones_notas.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_evaluaciones_notas.repository.CalificacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Service
@RequiredArgsConstructor
@Slf4j
public class CalificacionService {
    
    private final CalificacionRepository calificacionRepository;
    private final EvaluacionService evaluacionService;
    private final AcademicoClient academicoClient;
    
    @Transactional
    public Calificacion crear(Long evaluacionId, Long estudianteId, BigDecimal resultado) {
        Evaluacion evaluacion = evaluacionService.obtenerPorId(estudianteId);

        validarSubperiodoAbierto(evaluacion.getSubperiodoAcademicoId());

        if(calificacionRepository.existsByEvaluacionIdAndEstudianteId(evaluacionId, estudianteId)) {
            throw new RecursoDuplicadoException(
                "El estudiante id=" + estudianteId + " ya tiene una calificacion para la evaluacion id=" + evaluacionId
            );
        }

        Calificacion calificacion = calificacionRepository.save(
            new Calificacion(evaluacion, estudianteId, resultado));
        log.info("Calificacion creada: id={}, evaluacionId={}, estudianteId={}", calificacion.getId(), evaluacionId, estudianteId);
        return calificacion;
    }

    @Transactional
    public void modificar(Long calificacionId, BigDecimal nuevoResultado) {
        Calificacion calificacion = calificacionRepository.findByIdConEvaluacion(calificacionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Calificacion", calificacionId));
        
        validarSubperiodoAbierto(calificacion.getEvaluacion().getSubperiodoAcademicoId());
        
        calificacion.modificar(nuevoResultado);
        log.info("La calificacion modificada: id={}, nuevoResultado={}", calificacionId, nuevoResultado);
    }

    @Transactional(readOnly = true)
    public List<Calificacion> obtenerPorEstudiante(Long estudianteId) {
        return calificacionRepository.findByEstudianteIdConEvaluacion(estudianteId);
    }


    private void validarSubperiodoAbierto(Long subperiodoAcademicoId) {
        SubperiodoAcademicoClientResponse subperiodo = academicoClient.obtenerSubperiodo(subperiodoAcademicoId);
        if(!"ABIERTO".equals(subperiodo.estado())) {
            throw new RecursoInvalidoException(
                "El subperiodo id=" + subperiodoAcademicoId + " esta CERRADO. Use una Solicitud de exception para modificar esta calificacion."
            );
        }
    }

}
