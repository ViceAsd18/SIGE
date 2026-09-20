package cl.sige.plataforma.ms_evaluaciones_notas.event;

import java.time.LocalDate;

public record EvaluacionCreadaEvent(
    Long evaluacionId,
    Long asignacionDocenteId,
    Long subperiodoAcademicoId,
    String nombre,
    LocalDate fecha
) {}
