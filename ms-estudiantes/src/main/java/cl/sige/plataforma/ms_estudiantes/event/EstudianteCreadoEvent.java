package cl.sige.plataforma.ms_estudiantes.event;

public record EstudianteCreadoEvent(
    Long estudianteId,
    Long personaRolId
) {}
