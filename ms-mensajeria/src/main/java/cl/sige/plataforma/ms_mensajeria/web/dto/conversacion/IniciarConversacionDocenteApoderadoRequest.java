package cl.sige.plataforma.ms_mensajeria.web.dto.conversacion;

public record IniciarConversacionDocenteApoderadoRequest(
    Long docentePersonaId,
    Long docentePersonaRolId,
    Long apoderadoPersonaId,
    Long apoderadoPersonaRolId,
    Long estudianteId
) {}
