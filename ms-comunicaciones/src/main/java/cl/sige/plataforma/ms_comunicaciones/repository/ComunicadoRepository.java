package cl.sige.plataforma.ms_comunicaciones.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_comunicaciones.domain.Comunicado;

public interface ComunicadoRepository extends JpaRepository<Comunicado, Long> {
    
}
