package cl.sige.plataforma.ms_mensajeria.repository;

import cl.sige.plataforma.ms_mensajeria.domain.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MensajeRepository extends JpaRepository<Mensaje, Long> {

    @Query("SELECT m FROM Mensaje m JOIN FETCH m.conversacion WHERE m.conversacion.id = :conversacionId ORDER BY m.fechaEnvio ASC")
    List<Mensaje> findByConversacionIdOrdenados(@Param("conversacionId") Long conversacionId);

    @Query("SELECT m FROM Mensaje m JOIN FETCH m.conversacion WHERE m.id = :id")
    Optional<Mensaje> findByIdConConversacion(@Param("id") Long id);
}