package cl.sige.plataforma.ms_estudiantes.exception;

public class PersonaRolInvalidoException extends RuntimeException {
    public PersonaRolInvalidoException(String mensaje) {
        super(mensaje);
    }
}