package cl.sige.plataforma.ms_identidad_acceso.service;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import cl.sige.plataforma.ms_identidad_acceso.exception.CredencialesInvalidasException;
import cl.sige.plataforma.ms_identidad_acceso.repository.PersonaRepository;
import cl.sige.plataforma.ms_identidad_acceso.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AutenticacionService {

    private final PersonaRepository personaRepository;
    private final PersonaRolService personaRolService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public ResultadoAutenticacion autenticar(String usuario, String passwordPlano) {
        Persona persona = personaRepository.findByUsuario(usuario)
                .orElseThrow(CredencialesInvalidasException::new);

        if (!passwordEncoder.matches(passwordPlano, persona.getPasswordHash())) {
            throw new CredencialesInvalidasException();
        }

        List<PersonaRol> rolesActivos = personaRolService.obtenerRolesActivos(persona.getId());
        String token = jwtService.generar(persona, rolesActivos);

        log.info("Login exitoso: usuario={}, personaId={}", usuario, persona.getId());
        return new ResultadoAutenticacion(persona, token, rolesActivos);
    }
    
}