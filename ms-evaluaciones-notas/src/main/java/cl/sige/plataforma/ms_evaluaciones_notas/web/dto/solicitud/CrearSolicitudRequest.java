package cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud;

import java.math.BigDecimal;

public record CrearSolicitudRequest(
    Long calificacionId,
    Long solicitantePersonaRolId,
    String motivo,
    BigDecimal nuevoResultado
) {}
