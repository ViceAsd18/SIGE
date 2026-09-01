package cl.sige.plataforma.ms_calendario_reuniones.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_calendario_reuniones.domain.Reunion;

public interface ReunionRepository extends JpaRepository<Reunion, Long> {
    List<Reunion> findByFechaBetween(LocalDate desde, LocalDate hasta);
}
