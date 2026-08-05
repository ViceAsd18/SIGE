package cl.sige.plataforma.ms_identidad_acceso.service;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.exception.RecursoDuplicadoException;
import cl.sige.plataforma.ms_identidad_acceso.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_identidad_acceso.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Persona crear(NuevaPersonaComando comando) {
        
        if (personaRepository.existsByRutDocumentoIdentidad(comando.rutDocumentoIdentidad())) {
            throw new RecursoDuplicadoException("rutDocumentoIdentidad", comando.rutDocumentoIdentidad());
        }
        if (personaRepository.existsByEmail(comando.email())) {
            throw new RecursoDuplicadoException("email", comando.email());
        }
        if (personaRepository.existsByUsuario(comando.usuario())) {
            throw new RecursoDuplicadoException("usuario", comando.usuario());
        }

        String passwordHash = passwordEncoder.encode(comando.passwordPlano());

        Persona persona = Persona.builder()
                .rutDocumentoIdentidad(comando.rutDocumentoIdentidad())
                .nombres(comando.nombres())
                .apellidos(comando.apellidos())
                .fechaNacimiento(comando.fechaNacimiento())
                .email(comando.email())
                .telefono(comando.telefono())
                .usuario(comando.usuario())
                .passwordHash(passwordHash)
                .build();

        Persona guardada = personaRepository.save(persona);
        log.info("Persona creada: id={}, usuario={}", guardada.getId(), guardada.getUsuario());
        return guardada;
    }


 
    @Transactional(readOnly = true)
    public Persona obtenerPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Persona", id));
    }

}