package cl.sige.plataforma.ms_academico.service;

import cl.sige.plataforma.ms_academico.client.EstudiantesClient;
import cl.sige.plataforma.ms_academico.domain.Curso;
import cl.sige.plataforma.ms_academico.domain.Matricula;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoMatricula;
import cl.sige.plataforma.ms_academico.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_academico.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_academico.repository.MatriculaRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final CursoService cursoService;
    private final EstudiantesClient estudiantesClient;

    @Transactional
    public Matricula crear(Long estudianteId, Long cursoId, LocalDate fechaInicioVigencia) {
        validarEstudiante(estudianteId);
        Curso curso = cursoService.obtenerPorId(cursoId);

        matriculaRepository.findByEstudianteIdAndEstado(estudianteId, EstadoMatricula.ACTIVA)
                .ifPresent(m -> {
                    throw new RecursoDuplicadoException(
                            "El estudiante id=" + estudianteId + " ya tiene una matricula ACTIVA (id="
                                    + m.getId() + "). Use cambiarCurso() en vez de crear una nueva.");
                });

        Matricula matricula = matriculaRepository.save(
                new Matricula(estudianteId, curso, fechaInicioVigencia));
        log.info("Matricula creada: id={}, estudianteId={}, cursoId={}",
                matricula.getId(), estudianteId, cursoId);
        return matricula;
    }

    @Transactional
    public Matricula cambiarCurso(Long matriculaActivaId, Long nuevoCursoId, String motivo) {
        Matricula matriculaActual = matriculaRepository.findById(matriculaActivaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matricula", matriculaActivaId));

        if (matriculaActual.getEstado() != EstadoMatricula.ACTIVA) {
            throw new RecursoInvalidoException(
                    "La matricula id=" + matriculaActivaId + " no esta ACTIVA (estado actual: "
                            + matriculaActual.getEstado() + ")");
        }

        Curso nuevoCurso = cursoService.obtenerPorId(nuevoCursoId);

        matriculaActual.finalizar(motivo);

        Matricula nuevaMatricula = matriculaRepository.save(
                new Matricula(matriculaActual.getEstudianteId(), nuevoCurso, LocalDate.now()));

        log.info("Cambio de curso: matricula anterior id={} finalizada, nueva matricula id={}",
                matriculaActivaId, nuevaMatricula.getId());
        return nuevaMatricula;
    }

    @Transactional
    public void retirar(Long matriculaId, String motivo) {
        Matricula matricula = matriculaRepository.findById(matriculaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Matricula", matriculaId));
        matricula.retirar(motivo);
        log.info("Matricula retirada: id={}", matriculaId);
    }

    private void validarEstudiante(Long estudianteId) {
        try {
            estudiantesClient.obtenerEstudiante(estudianteId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException(
                    "No existe un Estudiante con id=" + estudianteId + " en ms-estudiantes");
        }
    }
}