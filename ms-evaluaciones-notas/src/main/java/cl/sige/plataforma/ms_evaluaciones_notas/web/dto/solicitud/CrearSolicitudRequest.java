package cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud;

public record CrearSolicitudRequest(
    Long calificacionId,
    Long solicitantePersonaRolId,
    String motivo
) {}
