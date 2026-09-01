package cl.sige.plataforma.ms_calendario_reuniones.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import cl.sige.plataforma.ms_calendario_reuniones.domain.ReunionParticipante;
import feign.Param;

public interface ReunionParticipanteRepository extends JpaRepository<ReunionParticipante, Long> {
    @Query("SELECT rp FROM ReunionParticipante rp JOIN FETCH rp.reunion WHERE rp.reunion.id = :reunionId")
    List<ReunionParticipante> findByReunionId(@Param("reunionId") Long reunionId);
}
