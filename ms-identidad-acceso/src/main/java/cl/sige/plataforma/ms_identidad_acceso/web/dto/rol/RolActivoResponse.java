package cl.sige.plataforma.ms_identidad_acceso.web.dto.rol;

import java.time.LocalDate;

public record RolActivoResponse(
    Long personaRolId, 
    String nombreRol, 
    LocalDate fechaInicio
) {}

