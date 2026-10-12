package cl.sige.plataforma.ms_calendario_reuniones.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_calendario_reuniones.domain.EvaluacionCalendario;

public interface EvaluacionCalendarioRepository extends JpaRepository<EvaluacionCalendario, Long> {
    List<EvaluacionCalendario> findByFechaBetween(LocalDate desde, LocalDate hasta);
}
