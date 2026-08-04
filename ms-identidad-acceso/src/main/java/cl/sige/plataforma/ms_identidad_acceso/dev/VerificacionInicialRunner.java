package cl.sige.plataforma.ms_identidad_acceso.dev;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import cl.sige.plataforma.ms_identidad_acceso.repository.RolRepository;
import cl.sige.plataforma.ms_identidad_acceso.service.NuevaPersonaComando;
import cl.sige.plataforma.ms_identidad_acceso.service.PersonaRolService;
import cl.sige.plataforma.ms_identidad_acceso.service.PersonaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

// TEMPORAL: eliminar este archivo cuando exista el primer controlador REST real.
@Component
@RequiredArgsConstructor
@Slf4j
public class VerificacionInicialRunner implements CommandLineRunner {

    private final PersonaService personaService;
    private final PersonaRolService personaRolService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("Roles cargados por Flyway: {}", rolRepository.count());

        NuevaPersonaComando comando = new NuevaPersonaComando(
                "11111111-1", "Juan", "Pérez",
                LocalDate.of(1980, 5, 10),
                "juan.perez@sige.cl", "+56911111111",
                "jperez", "claveSegura123"
        );

        Persona persona = personaService.crear(comando);

        boolean passwordValida = passwordEncoder.matches("claveSegura123", persona.getPasswordHash());
        log.info("Hash generado: {}", persona.getPasswordHash());
        log.info("Verificacion de password con matches(): {}", passwordValida);

        PersonaRol personaRol = personaRolService.asignarRol(persona.getId(), "DOCENTE");

        log.info("Persona id={}, PersonaRol id={}, estado={}",
                persona.getId(), personaRol.getId(), personaRol.getEstado());
    }
}