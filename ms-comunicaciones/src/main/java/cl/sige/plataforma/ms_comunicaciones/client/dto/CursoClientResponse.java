package cl.sige.plataforma.ms_comunicaciones.client.dto;

public record CursoClientResponse(
    Long id,
    Long nivelEducativoId,
    Long periodoAcademicoId,
    String paralelo
) {}
