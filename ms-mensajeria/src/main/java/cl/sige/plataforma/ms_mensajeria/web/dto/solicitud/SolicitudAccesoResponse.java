package cl.sige.plataforma.ms_mensajeria.web.dto.solicitud;

public record SolicitudAccesoResponse(
    Long id,
    Long conversacionId,
    Long solicitantePersonaRolId,
    Long aprobadorPersonaRolId,
    String motivo,
    String estado
) {}
