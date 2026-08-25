package cl.sige.plataforma.ms_mensajeria.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_mensajeria.domain.ConversacionParticipante;

public interface ConversacionParticipanteRepository extends JpaRepository<ConversacionParticipante, Long> {
    
    List<ConversacionParticipante> findByConversacionId(Long conversacionId);
    List<ConversacionParticipante> findByPersonaId(Long personaId);

    Optional<ConversacionParticipante> findByConversacionIdAndPersonaId(Long conversacionId, Long personaId);
    boolean existsByConversacionIdAndPersonaId(Long conversacionId, Long personaId);

}
