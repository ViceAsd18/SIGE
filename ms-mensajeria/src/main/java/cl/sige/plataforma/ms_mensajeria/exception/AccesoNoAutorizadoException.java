package cl.sige.plataforma.ms_mensajeria.exception;

public class AccesoNoAutorizadoException extends RuntimeException {
    public AccesoNoAutorizadoException(String mensaje) { super(mensaje); }
}