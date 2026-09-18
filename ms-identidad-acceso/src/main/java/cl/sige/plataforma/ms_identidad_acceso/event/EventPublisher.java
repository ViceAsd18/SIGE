package cl.sige.plataforma.ms_identidad_acceso.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j
public class EventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarPersonaCreada(PersonaCreadaEvent evento) {
        kafkaTemplate.send("identidad.eventos", evento.personaId().toString(), evento);
        log.info("Evento publicado: PersonaCreada, personaId={}", evento.personaId());
    }

    public void publicarRolAsignado(RolAsignadoEvent evento) {
        kafkaTemplate.send("identidad.eventos", evento.personaId().toString(), evento);
        log.info("Evento publicado: RolAsignado, personaRolId={}", evento.personaRolId());
    }

    public void publicarAuditoria(AuditoriaEvent evento) {
        kafkaTemplate.send("auditoria.eventos", evento.entidadAfectadaId().toString(), evento);
            log.info("Evento de auditoria publicado: accion={}, entidad={}#{}", 
                evento.accion(), evento.entidadAfectadaTipo(), evento.entidadAfectadaId());
    }

}
