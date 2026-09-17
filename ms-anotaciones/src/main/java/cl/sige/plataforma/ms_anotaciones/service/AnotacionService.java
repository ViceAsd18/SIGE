package cl.sige.plataforma.ms_anotaciones.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_anotaciones.client.EstudiantesClient;
import cl.sige.plataforma.ms_anotaciones.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_anotaciones.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_anotaciones.domain.Anotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.CategoriaAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.GravedadAnotacion;
import cl.sige.plataforma.ms_anotaciones.event.AnotacionCreadaEvent;
import cl.sige.plataforma.ms_anotaciones.event.AuditoriaEvent;
import cl.sige.plataforma.ms_anotaciones.event.EventPublisher;
import cl.sige.plataforma.ms_anotaciones.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_anotaciones.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_anotaciones.repository.AnotacionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnotacionService {
    
    private static final Set<String> ROLES_AUTORIZADOS = Set.of("DOCENTE", "INSPECTOR");

    private final AnotacionRepository anotacionRepository;
    private final EstudiantesClient estudiantesClient;
    private final IdentidadAccesoClient identidadAccesoClient;

    private final EventPublisher eventPublisher;

    @Transactional
    public Anotacion crear(Long estudianteId, Long autorPersonaRolId, CategoriaAnotacion categoria, GravedadAnotacion gravedad, String descripcion) {
        
        validarEstudiante(estudianteId);
        validarAutor(autorPersonaRolId);
        validarGravedad(categoria, gravedad);
        
        Anotacion anotacion = anotacionRepository.save(
            new Anotacion(estudianteId, autorPersonaRolId, categoria, gravedad, descripcion));

        eventPublisher.publicarAnotacionCreada(new AnotacionCreadaEvent(
            anotacion.getId(), estudianteId, autorPersonaRolId,
            categoria.name(), gravedad != null ? gravedad.name() : null, anotacion.getFechaCreacion()));

        eventPublisher.publicarAuditoria(new AuditoriaEvent(
            autorPersonaRolId, autorPersonaRolId, "CREAR", "Anotacion", anotacion.getId(),
            null, descripcion, null, Instant.now()));

        log.info("Anotacion creada: id={}, estudianteId={}, categoria={}", anotacion.getId(), estudianteId, categoria);
        return anotacion;
    }

    @Transactional
    public void modificar(Long id, String nuevaDescripcion) {
        Anotacion anotacion = obtenerPorId(id);
        anotacion.modificar(nuevaDescripcion);
        log.info("Anotacion modificada: id={}", id);
    }

    @Transactional
    public void anular(Long id) {
        Anotacion anotacion = obtenerPorId(id);
        anotacion.anular();
        log.info("Anotacion anulada: id={}", id);
    }

    @Transactional(readOnly = true)
    public Anotacion obtenerPorId(Long id) {
        return anotacionRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Anotacion", id));
    }

    @Transactional(readOnly = true)
    public List<Anotacion> obtenerPorEstudiante(Long estudianteId) {
        return anotacionRepository.findByEstudianteId(estudianteId);
    }








    private void validarEstudiante(Long estudianteId) {
        try {
            estudiantesClient.obtenerEstudiante(estudianteId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe Estudiante con id=" + estudianteId + " en ms-estudiantes");
        }
    }

    private void validarAutor(Long autorPersonaRolId) {
        PersonaRolClientResponse personaRol;
        try {
            personaRol = identidadAccesoClient.obtenerPersonaRol(autorPersonaRolId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe PersonaRol con id=" + autorPersonaRolId);
        }
        if(!"ACTIVO".equals(personaRol.estado())) {
            throw new RecursoInvalidoException("El personaRol con id=" + autorPersonaRolId + " no esta ACTIVO"); 
        }

        if(!ROLES_AUTORIZADOS.contains(personaRol.nombreRol())) {
            throw new RecursoInvalidoException("El rol " + personaRol.nombreRol() + " no esta autorizado para crear anotaciones (solo DOCENTE o INSPECTOR)");
        }
    }

    private void validarGravedad(CategoriaAnotacion categoria, GravedadAnotacion gravedad) {
        if (categoria == CategoriaAnotacion.NEGATIVA && gravedad == null) {
            throw new RecursoInvalidoException("Las anotaciones de categoria NEGATIVA requiren especificar gravedad");
        }

        if (categoria != CategoriaAnotacion.NEGATIVA && gravedad != null) {
            throw new RecursoInvalidoException("Solo las anotaciones de categoria NEGATIVA pueden tener gravedad");
        }
    }


}
