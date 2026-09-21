package cl.sige.plataforma.ms_mensajeria.event;

public record ConversacionIniciadaEvent(
    Long conversacionId,
    Long docentePersonaId,
    Long apoderadoPersonaId
) {}
