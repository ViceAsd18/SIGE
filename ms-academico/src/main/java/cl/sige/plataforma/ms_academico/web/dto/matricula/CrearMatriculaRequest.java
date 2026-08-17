package cl.sige.plataforma.ms_academico.web.dto.matricula;

import java.time.LocalDate;

public record CrearMatriculaRequest(
    Long estudianteId,
    Long cursoId,
    LocalDate fechaInicioVigencia
) {}
