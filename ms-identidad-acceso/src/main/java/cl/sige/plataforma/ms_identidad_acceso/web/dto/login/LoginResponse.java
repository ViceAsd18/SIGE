package cl.sige.plataforma.ms_identidad_acceso.web.dto.login;

import java.util.List;

public record LoginResponse(
    String token,
    String tipo,
    Long personaId,
    String usuario,
    List<String> roles
) {}