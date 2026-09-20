package cl.sige.plataforma.ms_academico.event;

import java.time.LocalDate;

public record MatriculaEvent(
    Long matriculaId,
    Long estudianteId,
    Long cursoId,
    String estado,
    LocalDate fecha
) {}
