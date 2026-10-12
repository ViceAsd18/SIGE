package cl.sige.plataforma.ms_calendario_reuniones.event;

import java.time.LocalDate;

/**
 * Lectura de academico.eventos (tipos mezclados).
 * SubperiodoAcademicoCerrado: trae subperiodoAcademicoId + nombre + fechaTermino.
 * PeriodoAcademicoCerrado: trae nombreAnio + fechaTermino.
 * Los demas eventos (curso, matricula...) no traen fechaTermino y se ignoran.
 */
public record AcademicoEventoRecibido(
    Long subperiodoAcademicoId,
    Long periodoAcademicoId,
    String nombre,
    String nombreAnio,
    LocalDate fechaTermino
) {}