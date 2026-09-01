package cl.sige.plataforma.ms_calendario_reuniones.web.dto.reunion;

import java.time.LocalDate;
import java.time.LocalTime;

import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.EstadoReunion;
import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoReunion;

public record ReunionResponse(
    Long id,
    TipoReunion tipo,
    LocalDate fecha,
    LocalTime horaInicio,
    LocalTime horaTermino,
    Long convocantePersonaRolId,
    EstadoReunion estado,
    String lugarModalidad
) {}
