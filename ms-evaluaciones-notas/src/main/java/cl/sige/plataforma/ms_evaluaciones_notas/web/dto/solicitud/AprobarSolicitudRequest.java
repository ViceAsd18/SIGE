package cl.sige.plataforma.ms_evaluaciones_notas.web.dto.solicitud;

import java.math.BigDecimal;

public record AprobarSolicitudRequest(
    Long aprobadorPersonaRolId,
    BigDecimal nuevoResultado
) {}
