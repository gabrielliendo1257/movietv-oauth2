package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.dto.request.MediaDto;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.service.interfaces.MessagingService;
import jakarta.jms.Destination;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessagingServiceImpl implements MessagingService
{
    private final JmsTemplate jmsTemplate;
    private final Destination netfreeQueue;

    public MessagingServiceImpl(JmsTemplate jmsTemplate, Destination netfreeQueue)
    {
        this.jmsTemplate = jmsTemplate;
        this.netfreeQueue = netfreeQueue;
    }

    @Override
    public void sendMovie(MediaDto media)
    {
        this.jmsTemplate.convertAndSend(
                this.netfreeQueue,
                media,
                message -> {
                    message.setStringProperty("X_MEDIA_SOURCE", "WEB");

                    return message;
                }
        );
    }
}
