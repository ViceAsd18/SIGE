package cl.sige.plataforma.ms_auditoria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sige.plataforma.ms_auditoria.domain.RegistroAuditoria;

public interface RegistroAuditoriaRepository extends  JpaRepository<RegistroAuditoria, Long>{
    List<RegistroAuditoria> findByEntidadAfectadaTipoAndEntidadAfectadaId(String entidadAfectadaTipo, Long entidadAfectadaId);
    List<RegistroAuditoria> findByPersonaId(Long personaId);
}
