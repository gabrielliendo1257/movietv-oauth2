package com.guille.media.reproductor.powercine.service.impl;

import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Component
@Profile(value = {"test"})
public class MessagingReceiverImpl //implements MessagingReceiver
{
    /*@Value("${powercine.env.messaging.queue}")
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
    }*/
}
