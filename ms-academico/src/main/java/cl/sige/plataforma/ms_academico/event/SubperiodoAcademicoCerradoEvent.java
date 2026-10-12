package cl.sige.plataforma.ms_academico.event;

import java.time.LocalDate;

public record SubperiodoAcademicoCerradoEvent(
    Long subperiodoAcademicoId,
    Long periodoAcademicoId,
    String nombre,
    LocalDate fechaTermino
) {}
