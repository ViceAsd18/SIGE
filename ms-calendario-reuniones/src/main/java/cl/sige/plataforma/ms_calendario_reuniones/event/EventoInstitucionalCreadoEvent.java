package cl.sige.plataforma.ms_calendario_reuniones.event;

import java.time.LocalDate;

public record EventoInstitucionalCreadoEvent(
    Long eventoInstitucionalId,
    String tipo,
    LocalDate fecha    
) {}
