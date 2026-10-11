package cl.sige.plataforma.ms_mensajeria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import cl.sige.plataforma.ms_mensajeria.domain.Conversacion;

public interface ConversacionRepository extends JpaRepository<Conversacion, Long> {

    @Query("SELECT c FROM ConversacionParticipante p JOIN p.conversacion c  WHERE p.personaId = :personaId ORDER BY c.fechaInicio DESC")
    List<Conversacion> findByParticipante(@Param("personaId") Long personaId);

}
