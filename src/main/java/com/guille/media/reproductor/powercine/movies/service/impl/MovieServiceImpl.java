package com.guille.media.reproductor.powercine.movies.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.movies.model.Movie;
import com.guille.media.reproductor.powercine.movies.repository.MovieRepository;
import com.guille.media.reproductor.powercine.movies.service.MovieService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<Movie> getAllMovies() throws IOException {
        return this.movieRepository.findAll();
    }

    @Override
    public Optional<Movie> getMovieById(Integer id) {
        return this.movieRepository.findById(id);
    }

    @Override
    public Optional<Movie> getMovieByTitle(String title) {
        return this.movieRepository.findMovieByTitle(title);
    }

}
