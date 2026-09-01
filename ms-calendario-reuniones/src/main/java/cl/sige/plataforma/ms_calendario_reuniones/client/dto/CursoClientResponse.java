package cl.sige.plataforma.ms_calendario_reuniones.client.dto;

public record CursoClientResponse(
    Long id,
    Long nivelEducativoId,
    Long periodoAcademicoId,
    String paralelo,
    Long profesorJefePersonaRolId
) {}
