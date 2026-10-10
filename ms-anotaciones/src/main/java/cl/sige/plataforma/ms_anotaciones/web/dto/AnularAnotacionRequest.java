package cl.sige.plataforma.ms_anotaciones.web.dto;

public record AnularAnotacionRequest(
    Long anuladorPersonaRolId,
    String motivo
) {}
