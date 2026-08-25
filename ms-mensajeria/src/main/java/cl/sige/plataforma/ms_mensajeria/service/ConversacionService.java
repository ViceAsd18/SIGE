package cl.sige.plataforma.ms_mensajeria.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_mensajeria.client.AcademicoClient;
import cl.sige.plataforma.ms_mensajeria.client.EstudiantesClient;
import cl.sige.plataforma.ms_mensajeria.client.IdentidadAccesoClient;
import cl.sige.plataforma.ms_mensajeria.client.dto.ApoderadoRelacionClientResponse;
import cl.sige.plataforma.ms_mensajeria.client.dto.AsignacionDocenteClientResponse;
import cl.sige.plataforma.ms_mensajeria.client.dto.PersonaRolClientResponse;
import cl.sige.plataforma.ms_mensajeria.domain.Conversacion;
import cl.sige.plataforma.ms_mensajeria.domain.ConversacionParticipante;
import cl.sige.plataforma.ms_mensajeria.exception.AccesoNoAutorizadoException;
import cl.sige.plataforma.ms_mensajeria.exception.RecursoInvalidoException;
import cl.sige.plataforma.ms_mensajeria.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_mensajeria.repository.ConversacionParticipanteRepository;
import cl.sige.plataforma.ms_mensajeria.repository.ConversacionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConversacionService {
    
    
    private final ConversacionRepository conversacionRepository;
    private final ConversacionParticipanteRepository participanteRepository;
    private final IdentidadAccesoClient indentidadAccesoClient;
    private final EstudiantesClient estudiantesClient;
    private final AcademicoClient academicoClient;


    @Transactional
    public Conversacion iniciarDocenteApoderado(
        Long docentePersonaId, Long docentePersonaRolId, 
        Long apoderadoPersonaId, Long apoderadoPersonaRolId,
        Long estudianteId
    ) {
        validarRolActivo(docentePersonaRolId, "DOCENTE");
        validarRolActivo(apoderadoPersonaRolId, "APODERADO");

        validarDocenteTieneAsignacionVigente(docentePersonaRolId);
        validarApoderadoTieneRelacionConEstudiante(apoderadoPersonaRolId, estudianteId);
        
        Conversacion conversacion = conversacionRepository.save(new Conversacion());
        participanteRepository.save(new ConversacionParticipante(conversacion, docentePersonaId));
        participanteRepository.save(new ConversacionParticipante(conversacion, apoderadoPersonaId));

        log.info("Conversacion iniciada id={}, docentePersonaId={}, apoderadoPersonaId={}", conversacion.getId(), docentePersonaId, apoderadoPersonaId);
        return conversacion;
        
    
    }


    @Transactional(readOnly = true)
    public Conversacion obtenerPorId(Long id) {
        return conversacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Conversacion", id));
    }

    @Transactional(readOnly = true)
    public void validarParticipante(Long conversacionId, Long personaId) {
        if(!participanteRepository.existsByConversacionIdAndPersonaId(conversacionId, personaId)) {
            throw new AccesoNoAutorizadoException("La persona id=" + personaId + " no es un participante de la conversacion id=" + conversacionId);
        }
    }



    private void validarRolActivo(Long personaRolId, String rolEsperado) {
        PersonaRolClientResponse personaRol;
        try {
            personaRol = indentidadAccesoClient.obtenerPersonaRol(personaRolId);
        } catch (FeignException.NotFound e) {
            throw new RecursoInvalidoException("No existe PersonaRol con id=" + personaRolId);
        }

        if(!"ACTIVO".equals(personaRol.estado()) || !rolEsperado.equals(personaRol.nombreRol())) {
            throw new RecursoInvalidoException("El PersonaRol id=" + personaRolId + " no es un " + rolEsperado + " activo valido");
        }

    }


    private void validarDocenteTieneAsignacionVigente(Long docentePersonaRolId) {
        List<AsignacionDocenteClientResponse> asignaciones = academicoClient.buscarPorDocente(docentePersonaRolId);
        boolean tieneVigente = asignaciones.stream().anyMatch(a -> "VIGENTE".equals(a.estado()));
        if (tieneVigente) {
            throw new AccesoNoAutorizadoException("El Docente con personaRolId=" + docentePersonaRolId + " no tiene ninguna Asignacion vigente");
        }
    }

    private void validarApoderadoTieneRelacionConEstudiante(Long apoderadoPersonaRolId, Long estudianteId) {
        List<ApoderadoRelacionClientResponse> apoderados = estudiantesClient.obtenerApoderadosDeEstudiante(estudianteId);
        boolean tieneRelacion = apoderados.stream()
                .anyMatch(a -> a.personaRolId().equals(apoderadoPersonaRolId));
        if (!tieneRelacion) {
            throw new AccesoNoAutorizadoException(
                    "El Apoderado no tiene relacion con el estudiante id=" + estudianteId);
        }
    }



}
