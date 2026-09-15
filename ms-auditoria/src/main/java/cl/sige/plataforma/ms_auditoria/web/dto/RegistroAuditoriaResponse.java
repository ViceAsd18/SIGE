package cl.sige.plataforma.ms_auditoria.web.dto;

import java.time.LocalDateTime;

public record RegistroAuditoriaResponse(
    Long id, Long personaId, Long rolActivoPersonaRolId, LocalDateTime fecha, String accion,
    String entidadAfectadaTipo, Long entidadAfectadaId, String valorAnterior, String valorNuevo, String motivo
) {}