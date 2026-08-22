package cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud;

public record SolicitudResponse(
    Long id,
    Long calificacionId,
    Long solicitantePersonaRolId,
    Long aprobadorPersonaRolId,
    String motivo,
    String estado
) {}
