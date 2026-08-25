package cl.sige.plataforma.ms_mensajeria.client.dto;

public record AsignacionDocenteClientResponse(
    Long id,
    Long docentePersonaRolId,
    Long asignaturaId,
    Long cursoId,
    String estado
) {}
