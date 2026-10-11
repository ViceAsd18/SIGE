package cl.sige.plataforma.ms_anotaciones.web.dto;

import java.util.List;

public record PaginaResponse<T>(
    List<T> contenido,
    int pagina,
    int tamano,
    long totalElementos,
    int totalPaginas
) {}
