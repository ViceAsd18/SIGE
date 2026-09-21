package cl.sige.plataforma.ms_mensajeria.event;

public record SolicitudAccesoEvent(
    Long solicitudId,
    Long conversacionId,
    Long solicitantePersonaRolId,
    Long aprobadorPersonaRolId,
    String estado
) {}
