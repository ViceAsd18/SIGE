package cl.sige.plataforma.ms_mensajeria.websocket;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import cl.sige.plataforma.ms_mensajeria.event.MensajeEnviadoEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component 
@RequiredArgsConstructor 
@Slf4j 
public class MensajeEventListener {
    
    private final SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "mensajeria.eventos", groupId = "ms-mensajeria-websocket")
    public void escucharMensajeEnviado(MensajeEnviadoEvent evento) {
        String destino = "/topic/conversaciones/" + evento.conversacionId();
        messagingTemplate.convertAndSend(destino,evento);
        log.info("Mensaje reenviado por WebSocket: conversacionId={}, destino={}", evento.conversacionId(), destino);
    }

}
