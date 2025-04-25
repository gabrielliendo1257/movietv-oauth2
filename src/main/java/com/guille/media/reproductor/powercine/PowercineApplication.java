package com.guille.media.reproductor.powercine;

import java.io.IOException;

import com.guille.media.reproductor.powercine.movies.bootstrap.BeanMapperMovies;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PowercineApplication {

    @Autowired
    private BeanMapperMovies beanMapperMovies;

    public static void main(String[] args) {
        SpringApplication.run(PowercineApplication.class, args);
    }

    @Bean
    public ApplicationRunner persistMovies() {
        return args -> {
            try {
                this.beanMapperMovies.saveMovies();
            } catch (IOException e) {
                System.err.println("ERROR: No se pudo persistir.");
                e.printStackTrace();
            }
        };
    }

}
