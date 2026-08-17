package cl.sige.plataforma.ms_academico.web.dto.asignacion_docente;

import java.time.LocalDate;

public record CrearAsignacionDocenteRequest(
    Long docentePersonaRolId,
    Long asignaturaId,
    Long cursoId,
    LocalDate fechaInicioVigencia
) {}
