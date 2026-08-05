package cl.sige.plataforma.ms_identidad_acceso.web.dto.persona;

import java.time.LocalDate;

public record PersonaResponse(
    Long id, 
    String rutDocumentoIdentidad, 
    String nombres, 
    String apellidos,
    LocalDate fechaNacimiento, 
    String email, 
    String telefono, 
    String usuario
) {}