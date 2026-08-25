package cl.sige.plataforma.ms_mensajeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_mensajeria.domain.SolicitudAccesoConversacion;

public interface SolicitudAccesoConversacionRepository extends JpaRepository<SolicitudAccesoConversacion, Long> { 
    
}
