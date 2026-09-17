package cl.sige.plataforma.ms_anotaciones.event;

import java.time.Instant;

public record AuditoriaEvent(
    Long personaId,
    Long rolActivoPersonaRolId,
    String accion,
    String entidadAfectadaTipo,
    Long entidadAfectadaId,
    String valorAnterior,
    String valorNuevo,
    String motivo,
    Instant fecha
) {}
