package cl.sige.plataforma.ms_academico.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.sige.plataforma.ms_academico.domain.PeriodoAcademico;
import cl.sige.plataforma.ms_academico.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_academico.repository.PeriodoAcademicoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PeriodoAcademicoService {
    
    private final PeriodoAcademicoRepository periodoAcademicoRepository;

    @Transactional
    public PeriodoAcademico crear(String nombreAnio, LocalDate fechaInicio, LocalDate fechaFin) {
        
        if (periodoAcademicoRepository.existsByNombreAnio(nombreAnio)){
            throw new RecursoDuplicadoException("Ya existe un PeriodoAcademico con nombreAnio = " + nombreAnio);    
        }

        PeriodoAcademico periodo = periodoAcademicoRepository.save(
            new PeriodoAcademico(nombreAnio, fechaInicio, fechaFin));
        log.info("PeriodoAcademico creado: id={}, nombreAnio={}", periodo.getId(), nombreAnio);
        return periodo;
    }

    @Transactional(readOnly = true)
    public PeriodoAcademico obtenerPorId(Long id) {
        return periodoAcademicoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("PeriodoAcademico", id));   
    }

    @Transactional(readOnly = true)
    public void cerrar(Long id){
        PeriodoAcademico periodo = obtenerPorId(id);
        periodo.cerrar();
        log.info("PeriodoAcademico cerrado: id={}",id);
    }

    @Transactional(readOnly = true)
    public void reabrir(Long id){
        PeriodoAcademico periodo = obtenerPorId(id);
        periodo.reabrir();
        log.info("PeriodoAcademico reabierto: id={}",id);
    }



}
