package cl.sige.plataforma.ms_evaluaciones_notas.client.dto;

public record SubperiodoAcademicoClientResponse(
    Long id,
    Long periodoAcademicoId,
    String nombre,
    String estado
) {}
