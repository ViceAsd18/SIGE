package cl.sige.plataforma.ms_mensajeria.web.dto.mensajes;

public record EditarMensajeRequest(
    Long autorPersonaId,
    String nuevoContenido
) {}
