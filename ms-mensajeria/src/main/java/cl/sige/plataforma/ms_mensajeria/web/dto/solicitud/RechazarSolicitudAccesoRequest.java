package cl.sige.plataforma.ms_mensajeria.web.dto.solicitud;

public record RechazarSolicitudAccesoRequest(
    Long aprobadorPersonaRolId,
    String motivo
) {}
