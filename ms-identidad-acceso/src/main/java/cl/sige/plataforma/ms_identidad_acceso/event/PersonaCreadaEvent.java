package cl.sige.plataforma.ms_identidad_acceso.event;

public record PersonaCreadaEvent(
    Long personaId,
    String usuario,
    String email
) {}
