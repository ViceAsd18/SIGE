package cl.sige.plataforma.ms_academico.web.dto.curso;

public record CursoResponse(
    Long id,
    Long nivelEducativoId,
    Long periodoAcademicoId,
    String paralelo,
    Long profesorJefePersonaRolId
) {}
