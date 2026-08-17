package cl.sige.plataforma.ms_academico.web.dto.matricula;

public record CambiarCursoRequest(
    Long nuevoCursoId,
    String motivo
) {}
