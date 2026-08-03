package cl.sige.plataforma.ms_identidad_acceso.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "persona_rol")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonaRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "persona_rol_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "persona_id", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoRol estado;

    public PersonaRol(Persona persona, Rol rol, LocalDate fechaInicio) {
        this.persona = persona;
        this.rol = rol;
        this.fechaInicio = fechaInicio;
        this.estado = EstadoRol.ACTIVO;
    }

    public void desactivar(LocalDate fechaTermino) {
        this.estado = EstadoRol.INACTIVO;
        this.fechaTermino = fechaTermino;
    }
}