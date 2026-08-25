package cl.sige.plataforma.ms_mensajeria.client.dto;

public record ApoderadoRelacionClientResponse(
    Long apoderadoId,
    Long personaRolId,
    String tipoRelacion
) {}
