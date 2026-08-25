package cl.sige.plataforma.ms_mensajeria.web.dto.mensajes;

import java.time.LocalDateTime;

public record MensajeResponse(
    Long id,
    Long conversacionId,
    Long autorPersonaId,
    String contenido,
    LocalDateTime fechaEnvio,
    LocalDateTime fechaUltimaModificacion
) {}
