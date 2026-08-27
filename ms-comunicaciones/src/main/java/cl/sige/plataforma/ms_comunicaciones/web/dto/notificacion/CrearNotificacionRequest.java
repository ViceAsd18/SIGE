package cl.sige.plataforma.ms_comunicaciones.web.dto.notificacion;

public record CrearNotificacionRequest(
    Long destinatarioPersonaId,
    String tipoEvento,
    String entidadOrigenTipo,
    Long entidadOrigenId,
    String contenido
) {}
