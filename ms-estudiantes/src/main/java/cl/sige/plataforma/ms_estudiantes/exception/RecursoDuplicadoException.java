package cl.sige.plataforma.ms_estudiantes.exception;

public class RecursoDuplicadoException extends RuntimeException {
    public RecursoDuplicadoException(String campo, Object valor) {
        super("Ya existe un registro con " + campo + " = " + valor);
    }

    public RecursoDuplicadoException(String mensaje) {
        super(mensaje);
    }

}