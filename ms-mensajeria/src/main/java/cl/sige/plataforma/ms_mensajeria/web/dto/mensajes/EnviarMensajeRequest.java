package cl.sige.plataforma.ms_mensajeria.web.dto.mensajes;

public record EnviarMensajeRequest(
    Long autorPersonaId,
    String contenido
) {}
