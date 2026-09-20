package cl.sige.plataforma.ms_academico.event;

import java.time.LocalDate;

public record AsignacionDocenteEvent(
    Long asignacionDocenteId,
    Long docentePersonaRolId,
    Long asignaturaId,
    Long cursoId,
    String tipo,
    LocalDate fecha
) {}
