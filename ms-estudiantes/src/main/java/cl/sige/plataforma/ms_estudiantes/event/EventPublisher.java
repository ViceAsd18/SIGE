package cl.sige.plataforma.ms_estudiantes.event;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j 
public class EventPublisher {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarEstudianteCreado(EstudianteCreadoEvent evento) {
        kafkaTemplate.send("estudiantes.eventos", evento.estudianteId().toString(), evento)
            .whenComplete((result, ex) -> {
                if(ex != null) {
                    log.error("FALLO al publicar EstudianteCreado, estudianteId={}", evento.estudianteId(), ex);
                } else {
                    log.info("Evento CONFIRMADO: EstudianteCreado, estudianteId={}, topic={}, offset={}",
                        evento.estudianteId(), result.getRecordMetadata().topic(), result.getRecordMetadata().offset());
                }
            });
    }

    public void publicarApoderadoEstudianteAsociado(ApoderadoEstudianteAsociadoEvent evento) {
        kafkaTemplate.send("estudiantes.eventos", evento.estudianteId().toString(), evento)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("FALLO al publicar ApoderadoEstudianteAsociado, id={}", evento.apoderadoEstudianteId(), ex);
                } else {
                    log.info("Evento CONFIRMADO: ApoderadoEstudianteAsociado, id={}, topic={}, offset={}",
                        evento.apoderadoEstudianteId(), result.getRecordMetadata().topic(), result.getRecordMetadata().offset());
                }
            });
    }
    
    public void publicarAuditoria(AuditoriaEvent evento) {
        kafkaTemplate.send("auditoria.eventos", evento.entidadAfectadaId().toString(), evento)
            .whenComplete((result, ex) -> {
                if(ex != null) {
                    log.error("FALLO al publicar auditoria, entidad={}#{}",
                        evento.entidadAfectadaTipo(), evento.entidadAfectadaId(), ex);
                } else {
                    log.info("Evento de auditoria CONFIRMADO: accion={}, entidad={}#{}, topic={}, offset={}",
                        evento.accion(), evento.entidadAfectadaTipo(), evento.entidadAfectadaId(),
                        result.getRecordMetadata().topic(), result.getRecordMetadata().offset());
                }
            });
    }

}
