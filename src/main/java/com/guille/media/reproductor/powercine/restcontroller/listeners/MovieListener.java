package com.guille.media.reproductor.powercine.restcontroller.listeners;

import com.guille.media.reproductor.powercine.dto.request.MediaDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MovieListener
{

    @JmsListener(destination = "${powercine.env.messaging.queue}")
    public void receiveMessage(MediaDto message)
    {
        log.info("Message received: {}", message);
    }
}
