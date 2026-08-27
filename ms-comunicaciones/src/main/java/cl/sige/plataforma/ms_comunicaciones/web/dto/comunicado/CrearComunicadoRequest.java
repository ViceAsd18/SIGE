package cl.sige.plataforma.ms_comunicaciones.web.dto.comunicado;

import cl.sige.plataforma.ms_comunicaciones.domain.enums.TipoAlcanceComunicado;

public record CrearComunicadoRequest(
    Long emisorPersonaRolId,
    TipoAlcanceComunicado tipoAlcance,
    Long cursoId,
    Long nivelEducativoId,
    String asunto,
    String contenido
) {}
