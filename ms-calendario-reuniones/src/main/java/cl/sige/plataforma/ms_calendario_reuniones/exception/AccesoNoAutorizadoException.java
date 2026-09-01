package cl.sige.plataforma.ms_calendario_reuniones.exception;

public class AccesoNoAutorizadoException extends RuntimeException {
    public AccesoNoAutorizadoException(String mensaje) { super(mensaje); }
}