package com.guille.media.reproductor.powercine.configuration;


//@Configuration
public class MessagingConf
{
    /*@Value("${powercine.env.messaging.queue}")
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
    }*/
}
