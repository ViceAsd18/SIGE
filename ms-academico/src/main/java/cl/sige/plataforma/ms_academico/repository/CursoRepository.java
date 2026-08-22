package cl.sige.plataforma.ms_academico.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_academico.domain.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

    boolean existsByNivelEducativoIdAndPeriodoAcademicoIdAndParalelo(Long nivelEducativoId, Long periodoAcademicoId, String paralelo);

}