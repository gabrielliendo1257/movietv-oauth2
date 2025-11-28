package com.guille.media.reproductor.powercine.restcontroller.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
public class WebSocketListener
{
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public WebSocketListener(SimpMessageSendingOperations simpMessageSendingOperations)
    {
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }

    @EventListener
    public void handleConnectionEstablished(SessionConnectedEvent event)
    {
        log.info("Established connection established");
    }

    @EventListener
    public void handleWebSocketDisconnect(SessionDisconnectEvent event)
    {
        log.info("Received WebSocket Disconnect event: {}", event);
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        log.info("Received WebSocket accessor: {}", accessor);
        String filename = (String) accessor.getSessionAttributes().get("filename");

        if (filename != null)
        {
            log.info("Received WebSocket Disconnect event: {}", filename);

            this.simpMessageSendingOperations.convertAndSend("/topic/public", "Mensage de prueba");
        }
    }
}
