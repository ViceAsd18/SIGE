package cl.sige.plataforma.ms_identidad_acceso.security;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;

    public String generar(Persona persona, List<PersonaRol> rolesActivos) {
        SecretKey key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));

        Instant ahora = Instant.now();
        Instant expiracion = ahora.plus(jwtProperties.expiracionMinutos(), ChronoUnit.MINUTES);

        List<Map<String, Object>> rolesClaim = rolesActivos.stream()
                .map(pr -> Map.<String, Object>of(
                        "personaRolId", pr.getId(),
                        "rol", pr.getRol().getNombreRol()
                ))
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(String.valueOf(persona.getId()))
                .claim("usuario", persona.getUsuario())
                .claim("roles", rolesClaim)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(expiracion))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
}