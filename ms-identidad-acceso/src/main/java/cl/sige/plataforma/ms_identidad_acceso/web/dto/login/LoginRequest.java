package cl.sige.plataforma.ms_identidad_acceso.web.dto.login;

public record LoginRequest(
    String usuario, String password
) {}