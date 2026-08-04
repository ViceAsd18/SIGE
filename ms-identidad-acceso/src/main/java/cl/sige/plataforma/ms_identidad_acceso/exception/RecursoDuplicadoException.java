package cl.sige.plataforma.ms_identidad_acceso.exception;

public class RecursoDuplicadoException extends RuntimeException {

    public RecursoDuplicadoException(String campo, String valor) {
        super("Ya existe un registro con " + campo + " = " + valor);
    }
}