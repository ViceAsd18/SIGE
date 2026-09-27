package cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud;

import java.math.BigDecimal;

public record SolicitudResponse(
    Long id,
    Long calificacionId,
    Long solicitantePersonaRolId,
    Long aprobadorPersonaRolId,
    String motivo,
    String estado,
    BigDecimal nuevoResultado
) {}
