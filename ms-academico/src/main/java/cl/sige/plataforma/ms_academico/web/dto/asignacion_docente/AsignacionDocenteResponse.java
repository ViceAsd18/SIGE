package cl.sige.plataforma.ms_academico.web.dto.asignacion_docente;

import java.time.LocalDate;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoVigencia;

public record AsignacionDocenteResponse(
    Long id,
    Long docentePersonaRolId,
    Long asignaturaId,
    Long cursoId,
    LocalDate fechaInicioVigencia,
    LocalDate fechaTerminoVigencia,
    EstadoVigencia estado
) {}
