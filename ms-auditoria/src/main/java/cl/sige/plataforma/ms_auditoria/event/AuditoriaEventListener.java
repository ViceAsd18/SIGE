package cl.sige.plataforma.ms_auditoria.event;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import cl.sige.plataforma.ms_auditoria.service.RegistroAuditoriaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j 
public class AuditoriaEventListener {
    
    private final RegistroAuditoriaService registroAuditoriaService;

    @KafkaListener(topics = "auditoria.eventos", groupId = "ms-auditoria")
    public void escucharEventoAuditoria(AuditoriaEvent evento) {
        log.info("Evento de auditoria recibido: accion={}, entidad={}#{}",
            evento.accion(), evento.entidadAfectadaTipo(), evento.entidadAfectadaId());

        registroAuditoriaService.registrar(
            evento.personaId(), evento.rolActivoPersonaRolId(),evento.accion(),
            evento.entidadAfectadaTipo(), evento.entidadAfectadaId(),
            evento.valorAnterior(), evento.valorNuevo(), evento.motivo()
        );

    }
}
