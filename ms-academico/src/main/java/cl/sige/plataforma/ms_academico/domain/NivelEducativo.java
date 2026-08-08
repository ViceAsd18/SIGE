package cl.sige.plataforma.ms_academico.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "nivel_educativo")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NivelEducativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nivel_educativo_id")
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    public NivelEducativo(String nombre) {
        this.nombre = nombre;
    }
}