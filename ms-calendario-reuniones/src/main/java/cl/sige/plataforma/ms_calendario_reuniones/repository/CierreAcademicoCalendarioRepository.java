package cl.sige.plataforma.ms_calendario_reuniones.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_calendario_reuniones.domain.CierreAcademicoCalendario;
import cl.sige.plataforma.ms_calendario_reuniones.domain.enums.TipoCierreAcademico;

public interface CierreAcademicoCalendarioRepository extends JpaRepository<CierreAcademicoCalendario, Long> {
    List<CierreAcademicoCalendario> findByFechaBetween(LocalDate desde, LocalDate hasta);
    Optional<CierreAcademicoCalendario> findByTipoAndOrigenId(TipoCierreAcademico tipo, Long origenId);
}
