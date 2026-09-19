package cl.sige.plataforma.ms_estudiantes.event;

public record ApoderadoEstudianteAsociadoEvent(
    Long apoderadoEstudianteId,
    Long apoderadoId, 
    Long estudianteId, 
    String tipoRelacion
) {}
