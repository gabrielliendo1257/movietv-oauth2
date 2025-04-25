package com.guille.media.reproductor.powercine.movies.restcontroller;

import java.io.IOException;
import java.util.List;

import com.guille.media.reproductor.powercine.movies.model.Movie;
import com.guille.media.reproductor.powercine.movies.service.MovieService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/v1/movie")
@RestController
public class MovieRestController {

    private final MovieService movieService;

    private MovieRestController(final MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllMovies() {
        try {
            return new ResponseEntity<List<Movie>>(this.movieService.getAllMovies(), HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
