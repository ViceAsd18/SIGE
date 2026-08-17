package cl.sige.plataforma.ms_academico.web.dto.subperiodo_academico;

import java.time.LocalDate;

public record CrearSubperiodoRequest(
    String nombre,
    LocalDate fechaInicio,
    LocalDate fechaTermino
) {}
