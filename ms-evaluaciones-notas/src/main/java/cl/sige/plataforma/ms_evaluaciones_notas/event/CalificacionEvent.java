package cl.sige.plataforma.ms_evaluaciones_notas.event;

import java.math.BigDecimal;

public record CalificacionEvent(
    Long calificacionId,
    Long evaluacionId,
    Long estudianteId,
    BigDecimal resultado,
    String tipo
) {}
