package cl.sige.plataforma.ms_identidad_acceso.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "sige.jwt")
public record JwtProperties(
    String secret, 
    long expiracionMinutos
) {}