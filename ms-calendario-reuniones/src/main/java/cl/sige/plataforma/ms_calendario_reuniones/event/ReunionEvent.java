package cl.sige.plataforma.ms_calendario_reuniones.event;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReunionEvent(
    Long reunionId,
    String tipo,
    LocalDate fecha,
    LocalTime horaInicio,
    Long convocantePersonaRolId,
    String estado
) {}
