package cl.sige.plataforma.ms_academico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_academico.domain.Matricula;
import cl.sige.plataforma.ms_academico.domain.enums.EstadoMatricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    Optional<Matricula> findByEstudianteIdAndEstado(Long estudianteId, EstadoMatricula estado);

}