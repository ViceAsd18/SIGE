package cl.sige.plataforma.ms_estudiantes.web.dto.relacion;
import cl.sige.plataforma.ms_estudiantes.domain.TipoRelacionApoderado;

public record AsociarEstudianteRequest(
    TipoRelacionApoderado tipoRelacion
) {}