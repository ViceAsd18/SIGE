package cl.sige.plataforma.ms_academico.web.dto.subperiodo_academico;

import java.time.LocalDate;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoPeriodo;

public record SubperiodoAcademicoResponse(
    Long id,
    Long periodoAcademicoId,
    String nombre,
    LocalDate fechaInicio,
    LocalDate fechatermino,
    EstadoPeriodo estado
) {}
