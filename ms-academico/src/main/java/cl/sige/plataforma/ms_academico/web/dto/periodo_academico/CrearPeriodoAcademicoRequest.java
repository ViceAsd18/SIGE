package cl.sige.plataforma.ms_academico.web.dto.periodo_academico;

import java.time.LocalDate;

public record CrearPeriodoAcademicoRequest(
    String nombreAnio,
    LocalDate fechaInicio,
    LocalDate fechaTermino
) {}
