package cl.sige.plataforma.ms_comunicaciones.service;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_comunicaciones.client.AcademicoClient;
import cl.sige.plataforma.ms_comunicaciones.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_comunicaciones.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_comunicaciones.domain.Comunicado;
import cl.sige.plataforma.ms_comunicaciones.domain.enums.TipoAlcanceComunicado;
import cl.sige.plataforma.ms_comunicaciones.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_comunicaciones.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_comunicaciones.repository.ComunicadoRepository;
import feign.FeignException;
import feign.FeignException.FeignClientException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComunicadoService {
    
    private static final Set<String> ROLES_EMISORES = Set.of("DIRECTIVO", "DOCENTE");

    private final ComunicadoRepository comunicadoRepository;
    private final IdentidadAccesoClient identidadAccesoClient;
    private final AcademicoClient academicoClient;

    @Transactional
    public Comunicado crear(Long emisorPersonaRolId, TipoAlcanceComunicado tipoAlcance, Long cursoId, Long nivelEducativoId, String asunto, String contenido) {
        validarEmisor(emisorPersonaRolId);
        validarAlcance(tipoAlcance, cursoId, nivelEducativoId);

        Comunicado comunicado = Comunicado.builder()
                .emisorPersonaRolId(emisorPersonaRolId)
                .tipoAlcance(tipoAlcance)
                .cursoId(cursoId)
                .nivelEducativoId(nivelEducativoId)
                .asunto(asunto)
                .contenido(contenido)
                .fechaPublicacion(LocalDateTime.now())
                .build();

        comunicado = comunicadoRepository.save(comunicado);
        log.info("Comunicado publicado: id={}, tipoAlcance={}", comunicado.getId(), tipoAlcance);
        return comunicado;
    }

    @Transactional(readOnly = true)
    public Comunicado obtenerPorId(Long id) {
        return comunicadoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Comunicado", id));
    }


    private void validarEmisor(Long emisorPersonaRolId) {
        PersonaRolClientResponse personaRol;
        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(emisorPersonaRolId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe PersoanRol con id=" + emisorPersonaRolId);
        }

        if (!"ACTIVO".equals(personaRol.estado()) || !ROLES_EMISORES.contains(personaRol.nombreRol())) {
            throw new RecursoInvalidoException("El rol " + personaRol.nombreRol() + " no esta autorizado para emitir comunicados");
        }

    }

    private void validarAlcance(TipoAlcanceComunicado tipoAlcance, Long cursoId, Long nivelEducativoId) {
        switch (tipoAlcance) {
            case CURSO -> {
                if (cursoId == null || nivelEducativoId != null) {
                    throw new RecursoInvalidoException(
                            "Alcance CURSO requiere cursoId y NO debe incluir nivelEducativoId");
                }
                try {
                    academicoClient.obtenerCurso(cursoId);
                } catch (FeignException.NotFound e) {
                    throw new RecursoInvalidoException("No existe Curso con id=" + cursoId + " en ms-academico");
                }
            }
            case NIVEL -> {
                if (nivelEducativoId == null || cursoId != null) {
                    throw new RecursoInvalidoException(
                            "Alcance NIVEL requiere nivelEducativoId y NO debe incluir cursoId");
                }
                try {
                    academicoClient.obtenerNivel(nivelEducativoId);
                } catch (FeignException.NotFound e) {
                    throw new RecursoInvalidoException("No existe NivelEducativo con id=" + nivelEducativoId + " en ms-academico");
                }
            }
            case ESTABLECIMIENTO -> {
                if (cursoId != null || nivelEducativoId != null) {
                    throw new RecursoInvalidoException(
                            "Alcance ESTABLECIMIENTO no debe incluir cursoId ni nivelEducativoId");
                }
            }
        }
    }




}
