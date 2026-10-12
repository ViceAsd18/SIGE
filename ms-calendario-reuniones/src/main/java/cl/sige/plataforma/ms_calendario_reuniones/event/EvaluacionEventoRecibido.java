package cl.sige.plataforma.ms_calendario_reuniones.event;

import java.time.LocalDate;

/** Lectura de evaluaciones.eventos. Solo EvaluacionCreada trae asignacionDocenteId y fecha. */
public record EvaluacionEventoRecibido(
    Long evaluacionId,
    Long asignacionDocenteId,
    Long subperiodoAcademicoId,
    String nombre,
    LocalDate fecha
) {}