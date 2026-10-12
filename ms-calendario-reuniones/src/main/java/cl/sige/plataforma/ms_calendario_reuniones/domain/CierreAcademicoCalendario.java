package cl.sige.plataforma.ms_calendario_reuniones.domain;

import java.time.LocalDate;

import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoCierreAcademico;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/** Proyeccion de solo lectura del cierre de un Subperiodo o Periodo (fuente: ms-academico). */
@Entity
@Table(name = "cierre_academico_calendario")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CierreAcademicoCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cierre_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoCierreAcademico tipo;

    @Column(name = "origen_id", nullable = false)
    private Long origenId;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    public CierreAcademicoCalendario(TipoCierreAcademico tipo, Long origenId, String nombre, LocalDate fecha) {
        this.tipo = tipo;
        this.origenId = origenId;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    public void actualizar(String nombre, LocalDate fecha) {
        this.nombre = nombre;
        this.fecha = fecha;
    }
}
