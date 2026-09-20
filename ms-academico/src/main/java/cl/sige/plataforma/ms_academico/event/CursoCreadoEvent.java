package cl.sige.plataforma.ms_academico.event;

public record CursoCreadoEvent(
    Long cursoId,
    Long nivelEducativoId,
    Long periodoAcademicoId,
    String paralelo
) {}
