package cl.sige.plataforma.ms_mensajeria.web.dto.solicitud;

public record CrearSolicitudAccesoRequest(
    Long conversacionId,
    Long solicitantePersonaRolId,
    String motivo
) {}
