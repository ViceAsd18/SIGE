package cl.sige.plataforma.ms_calendario_reuniones.web.dto.calendario;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Un elemento del calendario consolidado.
 * subtipo: tipo de reunion o de evento institucional (null en el resto).
 * estado: estado de la reunion (null en el resto).
 * horaInicio/horaTermino: solo reuniones; null = evento de dia completo.
 */
public record CalendarioItemResponse(
    TipoItemCalendario tipo,
    Long id,
    String titulo,
    String subtipo,
    String estado,
    LocalDate fecha,
    LocalTime horaInicio,
    LocalTime horaTermino
) {}