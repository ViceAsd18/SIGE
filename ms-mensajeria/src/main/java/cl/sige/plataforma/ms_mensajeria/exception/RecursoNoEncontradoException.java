package cl.sige.plataforma.ms_mensajeria.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String entidad, Object id) {
        super(entidad + " no encontrado(a) con identificador: " + id);
    }
}