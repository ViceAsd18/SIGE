package cl.sige.plataforma.ms_estudiantes.web.dto.apoderado_estudiante;

import cl.sige.plataforma.ms_estudiantes.domain.TipoRelacionApoderado;

public record ApoderadoEstudianteResponse(
    Long id, 
    Long apoderadoId, 
    Long estudianteId, 
    TipoRelacionApoderado tipoRelacion
) {}