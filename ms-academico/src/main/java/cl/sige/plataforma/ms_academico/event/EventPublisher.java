package cl.sige.plataforma.ms_academico.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarCursoCreado(CursoCreadoEvent evento) {
        enviar("academico.eventos", evento.cursoId().toString(), evento, "CursoCreado");
    }

    public void publicarAsignacionDocente(AsignacionDocenteEvent evento) {
        enviar("academico.eventos", evento.asignacionDocenteId().toString(), evento, "AsignacionDocente:" + evento.tipo());
    }

    public void publicarMatricula(MatriculaEvent evento) {
        enviar("academico.eventos", evento.matriculaId().toString(), evento, "Matricula:" + evento.estado());
    }

    public void publicarPeriodoAcademicoCerrado(PeriodoAcademicoCerradoEvent evento) {
        enviar("academico.eventos", evento.periodoAcademicoId().toString(), evento, "PeriodoAcademicoCerrado");
    }

    public void publicarSubperiodoAcademicoCerrado(SubperiodoAcademicoCerradoEvent evento) {
        enviar("academico.eventos", evento.subperiodoAcademicoId().toString(), evento, "SubperiodoAcademicoCerrado");
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