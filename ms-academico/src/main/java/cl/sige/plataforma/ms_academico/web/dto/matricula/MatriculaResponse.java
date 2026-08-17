package cl.sige.plataforma.ms_academico.web.dto.matricula;

import java.time.LocalDate;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoMatricula;

public record MatriculaResponse(
    Long id,
    Long estudianteId,
    Long cursoId,
    EstadoMatricula estado,
    String motivo,
    LocalDate fechaInicioVigencia,
    LocalDate fechaTerminoVigencia
) {}
