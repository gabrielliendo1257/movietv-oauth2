package com.guille.media.reproductor.powercine.configuration.cloud;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class S3Config {

    @Value("${powercine.env.minioapp.endpoint}")
    private String minioAddress;

    @Value("${powercine.env.minioapp.username}")
    private String minioUsername;

    @Value("${powercine.env.minioapp.password}")
    private String minioPassword;

    @Bean
    MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(this.minioAddress)
                .credentials(this.minioUsername, this.minioPassword)
                .build();
    }
}
