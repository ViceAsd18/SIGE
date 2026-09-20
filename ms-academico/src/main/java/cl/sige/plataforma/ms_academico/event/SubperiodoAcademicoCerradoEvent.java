package cl.sige.plataforma.ms_academico.event;

public record SubperiodoAcademicoCerradoEvent(
    Long subperiodoAcademicoId,
    Long periodoAcademicoId,
    String nombre
) {}
