package cl.sige.plataforma.ms_anotaciones.event;

import java.time.LocalDateTime;

public record AnotacionCreadaEvent (
    Long anotacionId,
    Long estudianteId,
    Long autorPersonaRolId,
    String categoria,
    String gravedad,
    LocalDateTime fechaCreacion
) {}
