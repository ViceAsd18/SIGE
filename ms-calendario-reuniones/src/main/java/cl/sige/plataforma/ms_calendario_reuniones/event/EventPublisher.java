package cl.sige.plataforma.ms_calendario_reuniones.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarReunion(ReunionEvent evento) {
        enviar("calendario.eventos", evento.reunionId().toString(), evento, "Reunion:" + evento.estado());
    }

    public void publicarEventoInstitucionalCreado(EventoInstitucionalCreadoEvent evento) {
        enviar("calendario.eventos", evento.eventoInstitucionalId().toString(), evento, "EventoInstitucionalCreado");
    }

    public void publicarAuditoria(AuditoriaEvent evento) {
        enviar("auditoria.eventos", evento.entidadAfectadaId().toString(), evento,
                "Auditoria:" + evento.accion() + ":" + evento.entidadAfectadaTipo());
    }

    private void enviar(String topico, String key, Object evento, String descripcion) {
        kafkaTemplate.send(topico, key, evento)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("FALLO al publicar {}, key={}, topico esperado={}", descripcion, key, topico, ex);
                    } else {
                        log.info("Evento CONFIRMADO: {}, key={}, topic={}, offset={}",
                                descripcion, key, result.getRecordMetadata().topic(), result.getRecordMetadata().offset());
                    }
                });
    }
}