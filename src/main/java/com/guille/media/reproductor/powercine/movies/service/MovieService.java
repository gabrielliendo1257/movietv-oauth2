package com.guille.media.reproductor.powercine.movies.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.movies.model.Movie;

public interface MovieService {

    List<Movie> getAllMovies() throws IOException;

    Optional<Movie> getMovieById(Integer id);

    Optional<Movie> getMovieByTitle(String title);

}
