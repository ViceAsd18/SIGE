package cl.sige.plataforma.ms_identidad_acceso.web.dto.persona_rol;

import java.time.LocalDate;
public record PersonaRolResponse (
    Long id, 
    Long personaId, 
    String nombreRol, 
    String estado,
    LocalDate fechaInicio, 
    LocalDate fechaTermino
) {}
