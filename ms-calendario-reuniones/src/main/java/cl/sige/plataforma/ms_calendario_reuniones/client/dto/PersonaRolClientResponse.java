package cl.sige.plataforma.ms_calendario_reuniones.client.dto;

import java.time.LocalDate;

public record PersonaRolClientResponse(
    Long id,
    Long personaId,
    String nombreRol,
    String estado,
    LocalDate fechaInicio, 
    LocalDate fechaTermino
) {}
