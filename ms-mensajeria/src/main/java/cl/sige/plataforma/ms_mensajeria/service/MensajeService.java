package cl.sige.plataforma.ms_mensajeria.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.sige.plataforma.ms_mensajeria.domain.Conversacion;
import cl.sige.plataforma.ms_mensajeria.domain.Mensaje;
import cl.sige.plataforma.ms_mensajeria.exception.AccesoNoAutorizadoException;
import cl.sige.plataforma.ms_mensajeria.exception.RecursoNoEncontradoException;
import cl.sige.plataforma.ms_mensajeria.repository.MensajeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MensajeService {
    
    private final MensajeRepository mensajeRepository;
    private final ConversacionService conversacionService;

    @Transactional
    public Mensaje enviar(Long conversacionId, Long autorPersonaId, String contenido) {
        Conversacion conversacion = conversacionService.obtenerPorId(conversacionId);
        conversacionService.validarParticipante(conversacionId, autorPersonaId);

        Mensaje mensaje = mensajeRepository.save(new Mensaje(conversacion, autorPersonaId, contenido));
        log.info("Mensaje enviado: id={}, conversacion={}, autorPersonaId={}", mensaje.getId(), conversacionId, autorPersonaId);
        return mensaje;
    }

    @Transactional
    public void editar(Long mensajeId, Long autorPersonaId, String nuevoContenido) {
        Mensaje mensaje = mensajeRepository.findByIdConConversacion(mensajeId)
            .orElseThrow(() -> new RecursoNoEncontradoException("Mensaje", mensajeId));
    
        if(!mensaje.getAutorPersonaId().equals(autorPersonaId)) {
            throw new AccesoNoAutorizadoException("Solo el autor puede editar su propio mensaje");
        }
        
        mensaje.editar(nuevoContenido);
        log.info("Mensaje editado: id={}", mensajeId);

    }

    @Transactional(readOnly = true)
    public List<Mensaje> obtenerPorConversacion(Long conversacionId) {
        return mensajeRepository.findByConversacionIdOrdenados(conversacionId);
    }
    

}
