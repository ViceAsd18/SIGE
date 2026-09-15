package cl.sige.plataforma.ms_auditoria.web.dto;

public record CrearRegistroAuditoriaRequest(
    Long personaId, Long rolActivoPersonaRolId, String accion, String entidadAfectadaTipo,
    Long entidadAfectadaId, String valorAnterior, String valorNuevo, String motivo
) {}