package cl.sige.plataforma.ms_comunicaciones.web.dto.comunicado;

import java.time.LocalDateTime;

import cl.sige.plataforma.ms_comunicaciones.domain.enums.TipoAlcanceComunicado;

public record ComunicadoResponse(
    Long id,
    Long emisorPersonaRolId,
    TipoAlcanceComunicado tipoAlcance,
    Long cursoId,
    Long nivelEducativoId,
    String asunto,
    String contenido,
    LocalDateTime fechaPublicacion
) {}
