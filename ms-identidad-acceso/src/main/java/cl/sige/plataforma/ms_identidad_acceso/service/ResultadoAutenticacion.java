package cl.sige.plataforma.ms_identidad_acceso.service;

import cl.sige.plataforma.ms_identidad_acceso.domain.Persona;
import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;

import java.util.List;

public record ResultadoAutenticacion(
    Persona persona,
    String token,
    List<PersonaRol> rolesActivos
) {}