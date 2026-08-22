package cl.sige.plataforma.ms_academico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.sige.plataforma.ms_academico.domain.NivelEducativo;

@Repository
public interface NivelEducativoRepository extends JpaRepository<NivelEducativo, Long> {
    
    public boolean existsByNombre(String nombre);

}
