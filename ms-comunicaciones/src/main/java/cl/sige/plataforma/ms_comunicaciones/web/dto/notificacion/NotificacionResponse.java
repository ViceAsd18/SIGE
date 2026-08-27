package cl.sige.plataforma.ms_comunicaciones.web.dto.notificacion;

import java.time.LocalDateTime;

public record NotificacionResponse(
    Long id,
    Long destinatarioPersonaId,
    String tipoEvento,
    String contenido,
    String estado,
    LocalDateTime fechaCreacion,
    LocalDateTime fechaLectura
) {}
