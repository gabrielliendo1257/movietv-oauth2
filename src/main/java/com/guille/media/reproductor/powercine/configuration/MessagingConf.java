package com.guille.media.reproductor.powercine.configuration;

import com.guille.media.reproductor.powercine.dto.request.MediaDto;
import jakarta.jms.Destination;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class MessagingConf
{
    @Value("${powercine.env.messaging.queue}")
    private String messagingQueue;

    @Bean
    Destination getDestination()
    {
        return new ActiveMQQueue(this.messagingQueue);
    }

    @Bean
    MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("_typeId");

        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("media", MediaDto.class);

        messageConverter.setTypeIdMappings(typeIdMappings);

        return messageConverter;
    }
}
