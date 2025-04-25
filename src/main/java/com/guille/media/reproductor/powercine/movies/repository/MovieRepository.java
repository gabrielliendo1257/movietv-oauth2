package com.guille.media.reproductor.powercine.movies.repository;

import java.util.Optional;

import com.guille.media.reproductor.powercine.movies.model.Movie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Optional<Movie> findMovieByTitle(String title);
}
