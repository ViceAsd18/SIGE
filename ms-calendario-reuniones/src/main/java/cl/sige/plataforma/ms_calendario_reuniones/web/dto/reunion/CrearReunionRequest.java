package cl.sige.plataforma.ms_calendario_reuniones.web.dto.reunion;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoReunion;

public record CrearReunionRequest(
    TipoReunion tipo,
    LocalDate fecha,
    LocalTime horaInicio,
    LocalTime horaTermino,
    Long convocantePersonaRolId,
    String lugarModalidad,
    Long cursoId,
    List<Long> participantesPersonaIds
) {}
