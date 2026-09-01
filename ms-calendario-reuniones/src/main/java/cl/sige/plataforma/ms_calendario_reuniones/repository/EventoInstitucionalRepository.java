package cl.sige.plataforma.ms_calendario_reuniones.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_calendario_reuniones.domain.EventoInstitucional;

public interface EventoInstitucionalRepository extends JpaRepository<EventoInstitucional, Long>{
    List<EventoInstitucional> findByFechaBetween(LocalDate desde, LocalDate hasta);
}
