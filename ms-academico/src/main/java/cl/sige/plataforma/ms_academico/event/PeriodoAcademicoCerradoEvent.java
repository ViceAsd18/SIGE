package cl.sige.plataforma.ms_academico.event;

import java.time.LocalDate;

public record PeriodoAcademicoCerradoEvent(
    Long periodoAcademicoId,
    String nombreAnio,
    LocalDate fechaTermino
) {}
