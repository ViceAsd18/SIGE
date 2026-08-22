package cl.sige.plataforma.ms_evaluaciones_notas.web.dto.calificacion;

import java.math.BigDecimal;

public record CrearCalificacionRequest(
    Long evaluacionId,
    Long estudianteId,
    BigDecimal resultado
) {}
