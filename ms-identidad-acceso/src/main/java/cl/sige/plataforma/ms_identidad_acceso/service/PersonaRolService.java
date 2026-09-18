package cl.sige.plataforma.ms_identidad_acceso.service;

import cl.sige.plataforma.ms_identidad_acceso.domain.EstadoRol;
import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import cl.sige.plataforma.ms_identidad_acceso.domain.Rol;
import cl.sige.plataforma.ms_identidad_acceso.event.AuditoriaEvent;
import cl.sige.plataforma.ms_identidad_acceso.event.EventPublisher;
import cl.sige.plataforma.ms_identidad_acceso.event.RolAsignadoEvent;
import cl.sige.plataforma.ms_identidad_acceso.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_identidad_acceso.repository.PersonaRepository;
import cl.sige.plataforma.ms_identidad_acceso.repository.PersonaRolRepository;
import cl.sige.plataforma.ms_identidad_acceso.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonaRolService {

    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PersonaRolRepository personaRolRepository;

    private final EventPublisher eventPublisher;

    @Transactional
    public PersonaRol asignarRol(Long personaId, String nombreRol) {
        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona", personaId));

        Rol rol = rolRepository.findByNombreRol(nombreRol)
                .orElseThrow(() -> new RecursoNoEncontradoException("Rol", nombreRol));

        PersonaRol personaRol = new PersonaRol(persona, rol, LocalDate.now());
        PersonaRol guardado = personaRolRepository.save(personaRol);

        eventPublisher.publicarRolAsignado(new RolAsignadoEvent(
            guardado.getId(), personaId, nombreRol));

        eventPublisher.publicarAuditoria(new AuditoriaEvent(
            personaId, guardado.getId(), "ASIGNAR_ROL", "PersonaRol", guardado.getId(),
            null, nombreRol, null, Instant.now()));

        log.info("Rol asignado: personaId={}, rol={}, personaRolId={}",
                personaId, nombreRol, guardado.getId());
        return guardado;
    }

    @Transactional
    public void desactivarRol(Long personaRolId) {
        PersonaRol personaRol = personaRolRepository.findById(personaRolId)
                .orElseThrow(() -> new RecursoNoEncontradoException("PersonaRol", personaRolId));

        personaRol.desactivar(LocalDate.now());
        log.info("Rol desactivado: personaRolId={}", personaRolId);
        // Sin save() explicito: dentro de @Transactional, la entidad esta
        // "managed" por JPA - el cambio de estado se sincroniza a la BD
        // automaticamente al finalizar la transaccion (dirty checking).
    }

    @Transactional(readOnly = true)
    public List<PersonaRol> obtenerRolesActivos(Long personaId) {
        return personaRolRepository.findByPersonaIdAndEstadoConRol(personaId, EstadoRol.ACTIVO);
    }

    @Transactional(readOnly = true)
    public PersonaRol obtenerPorId(Long id) {
        return personaRolRepository.findByIdConPersonaYRol(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("PersonaRol", id));
    }


}