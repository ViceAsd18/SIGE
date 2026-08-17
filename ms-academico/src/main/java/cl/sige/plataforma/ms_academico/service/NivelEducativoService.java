package cl.sige.plataforma.ms_academico.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_academico.domain.NivelEducativo;
import cl.sige.plataforma.ms_academico.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_academico.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_academico.repository.NivelEducativoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NivelEducativoService {
    
    private final NivelEducativoRepository nivelEducativoRepository;

    @Transactional
    public NivelEducativo crear(String nombre) {

        if(nivelEducativoRepository.existsByNombre(nombre)) {
            throw new RecursoDuplicadoException("El nivel educativo con nombre '" + nombre + "' ya existe.");
        }

        NivelEducativo nivel = nivelEducativoRepository.save(new NivelEducativo(nombre));
        log.info("NivelEducativo creado: id={}, nombre={}", nivel.getId(), nombre);
        return nivel;
    }

    @Transactional(readOnly = true)
    public NivelEducativo obtenerPorId(Long id) {
        return nivelEducativoRepository.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("NivelEducativo", id));
    }


}
