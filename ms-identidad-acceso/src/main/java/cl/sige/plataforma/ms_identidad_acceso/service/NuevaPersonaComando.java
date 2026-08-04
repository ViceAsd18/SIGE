package cl.sige.plataforma.ms_identidad_acceso.service;

import java.time.LocalDate;

public record NuevaPersonaComando(
        String rutDocumentoIdentidad,
        String nombres,
        String apellidos,
        LocalDate fechaNacimiento,
        String email,
        String telefono,
        String usuario,
        String passwordPlano
) {}