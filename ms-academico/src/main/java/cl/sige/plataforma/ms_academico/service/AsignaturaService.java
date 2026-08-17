package cl.sige.plataforma.ms_academico.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_academico.domain.Asignatura;
import cl.sige.plataforma.ms_academico.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_academico.repository.AsignaturaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsignaturaService {
    
    private final AsignaturaRepository asignaturaRepository;
    
    @Transactional
    public Asignatura crear(String nombre) {
        
        if(asignaturaRepository.existsByNombre(nombre)) {
            throw new RecursoDuplicadoException("Ya existe una asignatura con nombre = " + nombre);
        }

        Asignatura asignatura = asignaturaRepository.save(new Asignatura(nombre));
        log.info("Asignatura creada: id={}, nombre={}", asignatura.getId(), nombre);
        return asignatura;

    }

    @Transactional(readOnly = true)
    public Asignatura obtenerPorId(Long id) {
        return asignaturaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignatura", id));
    }

}
