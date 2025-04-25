package com.guille.media.reproductor.powercine.movies.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String uri;
    private String title;
    private String description;
    private Long duration;

    public Movie(String uri, String title, String description, Long duration) {
        this.uri = uri;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

}
