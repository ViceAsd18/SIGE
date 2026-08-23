package cl.sige.plataforma.ms_anotaciones.web.dto;

import java.time.LocalDateTime;

import cl.sige.plataforma.ms_anotaciones.domain.enums.CategoriaAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.EstadoAnotacion;
import cl.sige.plataforma.ms_anotaciones.domain.enums.GravedadAnotacion;

public record AnotacionResponse(
    Long id,
    Long estudianteId,
    Long autorPersonaRolId,
    CategoriaAnotacion categoria,
    GravedadAnotacion gravedad,
    String descripcion,
    LocalDateTime fechaCreacion,
    EstadoAnotacion estado
) {}
