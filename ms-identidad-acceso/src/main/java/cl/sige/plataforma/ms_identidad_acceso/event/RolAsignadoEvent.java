package cl.sige.plataforma.ms_identidad_acceso.event;

public record RolAsignadoEvent(
    Long personaRolId,
    Long personaId,
    String nombreRol
) {}
