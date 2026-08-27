package cl.sige.plataforma.ms_comunicaciones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_comunicaciones.domain.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByDestinatarioPersonaId(Long destinatarioPersonaId);
}
