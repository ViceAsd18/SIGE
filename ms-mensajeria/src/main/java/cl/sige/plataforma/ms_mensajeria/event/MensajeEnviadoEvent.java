package cl.sige.plataforma.ms_mensajeria.event;

public record MensajeEnviadoEvent(
    Long mensajeId,
    Long conversacoinId,
    Long autorPersonaId,
    String contenido
) {}
