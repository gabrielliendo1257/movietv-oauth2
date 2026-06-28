package com.guille.media.reproductor.powercine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableCaching
@SpringBootApplication
public class PowercineApplication {

    public static void main(String[] args) {
        SpringApplication.run(PowercineApplication.class, args);
    }

}
