package cl.sige.plataforma.ms_evaluaciones_notas.web.dto.evaluacion;

import java.time.LocalDate;

public record CrearEvaluacionRequest(
    Long asignacionDocenteId,
    Long subperiodoAcademicoId,
    String nombre,
    LocalDate fecha,
    Integer ponderacion
) {}
