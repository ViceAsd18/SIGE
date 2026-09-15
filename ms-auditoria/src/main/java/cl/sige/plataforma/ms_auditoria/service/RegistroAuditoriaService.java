package cl.sige.plataforma.ms_auditoria.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_auditoria.domain.RegistroAuditoria;
import cl.sige.plataforma.ms_auditoria.repository.RegistroAuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service 
@RequiredArgsConstructor 
@Slf4j 
public class RegistroAuditoriaService {
    
    private final RegistroAuditoriaRepository registroAuditoriaRepository;

    @Transactional
    public RegistroAuditoria registrar(Long personaId, Long rolActivoPersonaRolId, String accion, String entidadAfectadaTipo,
        Long entidadAfectadaId, String valorAnterior, String valorNuevo, String motivo
    ) {
        RegistroAuditoria registro = RegistroAuditoria.builder()
            .personaId(personaId)
            .rolActivoPersonaRolId(rolActivoPersonaRolId)
            .fecha(LocalDateTime.now())
            .accion(accion)
            .entidadAfectadaTipo(entidadAfectadaTipo)
            .entidadAfectadaId(entidadAfectadaId)
            .valorAnterior(valorAnterior)
            .valorNuevo(valorNuevo)
            .motivo(motivo)
            .build();

        registro = registroAuditoriaRepository.save(registro);
        log.info("RegistroAuditoria creado: id={}, accion={}, entidad={}#{}", registro.getId(), accion, entidadAfectadaTipo, entidadAfectadaId);
        return registro;
    }

    @Transactional(readOnly = true)
    public List<RegistroAuditoria> consultarPorEntidad(String entidadAfectadaTipo, Long entidadAfectadaId) {
        return registroAuditoriaRepository.findByEntidadAfectadaTipoAndEntidadAfectadaId(entidadAfectadaTipo, entidadAfectadaId);
    }

    @Transactional(readOnly = true)
    public List<RegistroAuditoria> consultarPorPersona(Long personaId) {
        return registroAuditoriaRepository.findByPersonaId(personaId);
    }

}
