package cl.sige.plataforma.ms_academico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sige.plataforma.ms_academico.domain.SubperiodoAcademico;

@Repository
public interface SubperiodoAcademicoRepository extends JpaRepository<SubperiodoAcademico, Long>{
    
    public List<SubperiodoAcademico> findByPeriodoAcademicoId(Long periodoAcademicoId);

}
