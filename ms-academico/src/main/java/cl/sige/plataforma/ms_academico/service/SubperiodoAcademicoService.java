package cl.sige.plataforma.ms_academico.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_academico.repository.SubperiodoAcademicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.sige.plataforma.ms_academico.domain.SubperiodoAcademico;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_academico.domain.PeriodoAcademico;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubperiodoAcademicoService {
    
    private final SubperiodoAcademicoRepository subperiodoAcademicoRepository;
    private final PeriodoAcademicoService periodoAcademicoService;

    @Transactional
    public SubperiodoAcademico crear(Long periodoAcademicoId, String nombre, LocalDate fechaInicio, LocalDate fechaTermino) {

        PeriodoAcademico periodo = periodoAcademicoService.obtenerPorId(periodoAcademicoId);

        SubperiodoAcademico subperiodo = subperiodoAcademicoRepository.save(
            new SubperiodoAcademico(periodo, nombre, fechaInicio, fechaTermino));
        log.info("SubperiodoAcademico creado: id={}, periodoId={}", subperiodo.getId(), periodoAcademicoId);
        return subperiodo;
    }

    @Transactional(readOnly = true)
    public List<SubperiodoAcademico> obtenerPorPeriodo(Long periodoAcademicoId) {
        return subperiodoAcademicoRepository.findByPeriodoAcademicoId(periodoAcademicoId);
    }

    @Transactional
    public void cerrar(Long id) {
        SubperiodoAcademico subperiodo = subperiodoAcademicoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("SubperiodoAcademico", id));
        subperiodo.cerrar();
    }

    @Transactional
    public void reabrir(Long id) {
        SubperiodoAcademico subperiodo = subperiodoAcademicoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("SubperiodoAcademico", id));
        subperiodo.reabrir();
    }

    @Transactional(readOnly = true)
    public SubperiodoAcademico obtenerPorId(Long id) {
        return subperiodoAcademicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("SubperiodoAcademico", id));
    }


}
