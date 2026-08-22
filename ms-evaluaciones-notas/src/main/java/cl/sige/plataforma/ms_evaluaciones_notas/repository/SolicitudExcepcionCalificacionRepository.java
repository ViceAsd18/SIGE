package cl.sige.plataforma.ms_evaluaciones_notas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_evaluaciones_notas.domain.SolicitudExcepcionCalificacion;
import cl.sige.plataforma.ms_evaluaciones_notas.domain.enums.EstadoSolicitud;;

public interface SolicitudExcepcionCalificacionRepository extends JpaRepository<SolicitudExcepcionCalificacion, Long> {
    List<SolicitudExcepcionCalificacion> findByEstado(EstadoSolicitud estado);
}
