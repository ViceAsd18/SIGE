package cl.sige.plataforma.ms_academico.web.dto.periodo_academico;

import java.time.LocalDate;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoPeriodo;

public record PeriodoAcademicoResponse(
    Long id,
    String nombreAnio,
    LocalDate fechaInicio,
    LocalDate fechaTermino,
    EstadoPeriodo estado
) {}
