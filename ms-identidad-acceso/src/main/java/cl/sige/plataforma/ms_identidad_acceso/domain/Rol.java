package cl.sige.plataforma.ms_identidad_acceso.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rol")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rol_id")
    private Long id;

    @Setter
    @Column(name = "nombre_rol", nullable = false, unique = true, length = 30)
    private String nombreRol;

    public Rol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}