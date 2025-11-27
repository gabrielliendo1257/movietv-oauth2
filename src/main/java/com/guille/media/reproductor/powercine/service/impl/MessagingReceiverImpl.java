package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.service.interfaces.MessagingReceiver;
import jakarta.jms.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessagingReceiverImpl implements MessagingReceiver
{
    @Value("${powercine.env.messaging.queue}")
    private String messagingQueue;

    private final JmsTemplate jmsTemplate;

    public MessagingReceiverImpl(JmsTemplate jmsTemplate)
    {
        this.jmsTemplate = jmsTemplate;
    }

    @Override
    public MediaJpaEntity receiverMedia()
    {
        return (MediaJpaEntity) this.jmsTemplate.receiveAndConvert(this.messagingQueue);
    }
}
