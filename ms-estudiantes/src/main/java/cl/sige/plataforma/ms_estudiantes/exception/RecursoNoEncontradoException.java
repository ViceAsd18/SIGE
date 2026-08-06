package cl.sige.plataforma.ms_estudiantes.exception;

public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String entidad, Object identificador) {
        super(entidad + " no encontrado(a) con identificador: " + identificador);
    }
}