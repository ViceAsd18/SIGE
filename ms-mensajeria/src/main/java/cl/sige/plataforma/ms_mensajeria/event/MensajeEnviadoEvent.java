package cl.sige.plataforma.ms_mensajeria.event;

public record MensajeEnviadoEvent(
    Long mensajeId,
    Long conversacionId,
    Long autorPersonaId,
    String contenido
) {}
