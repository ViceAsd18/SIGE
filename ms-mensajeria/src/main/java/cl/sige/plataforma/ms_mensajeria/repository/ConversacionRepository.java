package cl.sige.plataforma.ms_mensajeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_mensajeria.domain.Conversacion;

public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {
}
