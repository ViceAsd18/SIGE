package cl.sige.plataforma.ms_identidad_acceso.dev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import cl.sige.plataforma.ms_identidad_acceso.repository.PersonaRepository;
import cl.sige.plataforma.ms_identidad_acceso.repository.PersonaRolRepository;
import cl.sige.plataforma.ms_identidad_acceso.repository.RolRepository;

import java.time.LocalDate;

// TEMPORAL: eliminar este archivo cuando exista el primer controlador REST real.
@Component
public class VerificacionInicialRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(VerificacionInicialRunner.class);

    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PersonaRolRepository personaRolRepository;

    public VerificacionInicialRunner(PersonaRepository personaRepository,
                                      RolRepository rolRepository,
                                      PersonaRolRepository personaRolRepository) {
        this.personaRepository = personaRepository;
        this.rolRepository = rolRepository;
        this.personaRolRepository = personaRolRepository;
    }

    @Override
    public void run(String... args) {
        log.info("Roles cargados por Flyway: {}", rolRepository.count());

        Persona persona = Persona.builder()
                .rutDocumentoIdentidad("11111111-1")
                .nombres("Juan")
                .apellidos("Pérez")
                .fechaNacimiento(LocalDate.of(1980, 5, 10))
                .email("juan.perez@sige.cl")
                .telefono("+56911111111")
                .usuario("jperez")
                .passwordHash("hash-temporal-no-real")
                .build();
        personaRepository.save(persona);

        var rolDocente = rolRepository.findByNombreRol("DOCENTE")
                .orElseThrow(() -> new IllegalStateException("Rol DOCENTE no encontrado"));

        PersonaRol personaRol = new PersonaRol(persona, rolDocente, LocalDate.now());
        personaRolRepository.save(personaRol);

        log.info("Persona creada con id={}, PersonaRol creada con id={}",
                persona.getId(), personaRol.getId());
    }
}