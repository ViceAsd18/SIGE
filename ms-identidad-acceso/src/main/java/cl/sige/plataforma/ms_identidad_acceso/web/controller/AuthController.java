package cl.sige.plataforma.ms_identidad_acceso.web.controller;

import cl.sige.plataforma.ms_identidad_acceso.domain.PersonaRol;
import cl.sige.plataforma.ms_identidad_acceso.service.AutenticacionService;
import cl.sige.plataforma.ms_identidad_acceso.service.ResultadoAutenticacion;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.login.LoginRequest;
import cl.sige.plataforma.ms_identidad_acceso.web.dto.login.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AutenticacionService autenticacionService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        ResultadoAutenticacion resultado =
                autenticacionService.autenticar(request.usuario(), request.password());

        List<String> roles = resultado.rolesActivos().stream()
                .map(PersonaRol::getRol)
                .map(rol -> rol.getNombreRol())
                .toList();

        LoginResponse response = new LoginResponse(
                resultado.token(),
                "Bearer",
                resultado.persona().getId(),
                resultado.persona().getUsuario(),
                roles
        );

        return ResponseEntity.ok(response);
    }
}