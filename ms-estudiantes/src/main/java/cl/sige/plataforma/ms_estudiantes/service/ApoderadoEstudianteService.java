package cl.sige.plataforma.ms_estudiantes.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_estudiantes.domain.Apoderado;
import cl.sige.plataforma.ms_estudiantes.domain.ApoderadoEstudiante;
import cl.sige.plataforma.ms_estudiantes.domain.Estudiante;
import cl.sige.plataforma.ms_estudiantes.domain.TipoRelacionApoderado;
import cl.sige.plataforma.ms_estudiantes.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_estudiantes.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_estudiantes.repository.ApoderadoEstudianteRepository;
import cl.sige.plataforma.ms_estudiantes.repository.ApoderadoRepository;
import cl.sige.plataforma.ms_estudiantes.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApoderadoEstudianteService {

    private final ApoderadoRepository apoderadoRepository;
    private final EstudianteRepository estudianteRepository;
    private final ApoderadoEstudianteRepository apoderadoEstudianteRepository;

    @Transactional
    public ApoderadoEstudiante asociar(Long apoderadoId, Long estudianteId, TipoRelacionApoderado tipoRelacion) {
        Apoderado apoderado = apoderadoRepository.findById(apoderadoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Apoderado", apoderadoId));
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante", estudianteId));

        if (apoderadoEstudianteRepository.existsByApoderadoIdAndEstudianteId(apoderadoId, estudianteId)) {
            throw new RecursoDuplicadoException(
                    "Ya existe una relacion entre apoderado id=" + apoderadoId + " y estudiante id=" + estudianteId);
        }

        ApoderadoEstudiante relacion = apoderadoEstudianteRepository.save(
                new ApoderadoEstudiante(apoderado, estudiante, tipoRelacion));

        log.info("Relacion creada: apoderadoId={}, estudianteId={}, tipo={}",
                apoderadoId, estudianteId, tipoRelacion);
        return relacion;
    }

    @Transactional(readOnly = true)
    public List<ApoderadoEstudiante> obtenerEstudiantesDeApoderado(Long apoderadoId) {
        if (!apoderadoRepository.existsById(apoderadoId)) {
            throw new RecursoNoEncontradoException("Apoderado", apoderadoId);
        }
        return apoderadoEstudianteRepository.findByApoderadoIdConEstudiante(apoderadoId);
    }

    @Transactional(readOnly = true)
    public List<ApoderadoEstudiante> obtenerApoderadosDeEstudiante(Long estudianteId) {
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new RecursoNoEncontradoException("Estudiante", estudianteId);
        }
        return apoderadoEstudianteRepository.findByEstudianteIdConApoderado(estudianteId);
    }
}