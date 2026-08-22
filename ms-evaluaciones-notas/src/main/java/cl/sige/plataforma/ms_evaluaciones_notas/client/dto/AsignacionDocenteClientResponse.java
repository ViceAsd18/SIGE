package cl.sige.plataforma.ms_evaluaciones_notas.client.dto;

public record AsignacionDocenteClientResponse(
    Long id,
    Long docentePersonaRolId,
    Long asignaturaId,
    Long cursoId,
    String estado
) {}
