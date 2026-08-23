package cl.sige.plataforma.ms_anotaciones.web.dto;

import cl.sige.plataforma.ms_anotaciones.domain.enums.CategoriaAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.GravedadAnotacion;

public record CrearAnotacionRequest(
    Long estudianteId,
    Long autorPersonaRolId,
    CategoriaAnotacion categoria,
    GravedadAnotacion gravedad,
    String descripcion
) {}
