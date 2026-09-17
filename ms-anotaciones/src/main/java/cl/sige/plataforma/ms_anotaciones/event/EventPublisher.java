package cl.sige.plataforma.ms_anotaciones.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor
@Slf4j 
public class EventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarAnotacionCreada(AnotacionCreadaEvent evento) {
        kafkaTemplate.send("anotaciones.eventos", evento.anotacionId().toString(), evento);
        log.info("Evento publicado: Anotacion Creada, anotacionId={}", evento.anotacionId());
    }

    public void publicarAuditoria(AuditoriaEvent evento) {
        kafkaTemplate.send("auditoria.eventos", evento.entidadAfectadaId().toString(), evento);
        log.info("Evento de auditoria publicado: accion={}, entidad={}#{}", evento.accion(), evento.entidadAfectadaTipo(), evento.entidadAfectadaId());
    }

}
