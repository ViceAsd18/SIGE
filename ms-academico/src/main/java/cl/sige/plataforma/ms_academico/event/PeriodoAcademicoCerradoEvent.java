package cl.sige.plataforma.ms_academico.event;

public record PeriodoAcademicoCerradoEvent(
    Long periodoAcademicoId,
    String nombreAnio
) {}
