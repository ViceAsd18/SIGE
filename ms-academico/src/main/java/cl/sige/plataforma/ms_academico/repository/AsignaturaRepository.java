package cl.sige.plataforma.ms_academico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sige.plataforma.ms_academico.domain.Asignatura;

@Repository
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long>{
    
    public boolean existsByNombre(String nombre);

}
