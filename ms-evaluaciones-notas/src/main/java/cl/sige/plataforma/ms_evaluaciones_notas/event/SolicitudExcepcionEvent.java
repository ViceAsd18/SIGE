package cl.sige.plataforma.ms_evaluaciones_notas.event;

public record SolicitudExcepcionEvent(
    Long solicitudId,
    Long calificacionId,
    Long solicitantePersonaRolId,
    Long aprobadorPersonaRolId,
    String estado
) {}
