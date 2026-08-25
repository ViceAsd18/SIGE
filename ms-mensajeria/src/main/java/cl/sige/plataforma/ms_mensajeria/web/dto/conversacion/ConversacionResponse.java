package cl.sige.plataforma.ms_mensajeria.web.dto.conversacion;

import java.time.LocalDateTime;

public record ConversacionResponse(
    Long id,
    LocalDateTime fechaInicio
) {}
