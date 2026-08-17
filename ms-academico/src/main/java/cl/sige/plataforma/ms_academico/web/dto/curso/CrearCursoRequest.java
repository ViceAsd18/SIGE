package cl.sige.plataforma.ms_academico.web.dto.curso;

public record CrearCursoRequest(
    Long nivelEducativoId,
    Long periodoAcademicoId,
    String paralelo,
    Long profesorJefePersonaRolId
) {}