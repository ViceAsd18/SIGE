package cl.sige.plataforma.ms_calendario_reuniones.web.dto.evento_institucional;

import java.time.LocalDate;

import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoEventoInstitucional;

public record EventoInstitucionalResponse(
    Long id,
    TipoEventoInstitucional tipo,
    LocalDate fecha,
    String descripcion
) {}
