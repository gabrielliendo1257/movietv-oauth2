package com.guille.media.reproductor.powercine.movies.bootstrap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.guille.media.reproductor.powercine.movies.management.FileManagement;
import com.guille.media.reproductor.powercine.movies.model.Movie;
import com.guille.media.reproductor.powercine.movies.repository.MovieRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BeanMapperMovies {

    @Value(value = "${path.movies}")
    private String pathMovies;

    @Value(value = "${host.movies}")
    private String baseHost;

    private MovieRepository movieRepository;

    public BeanMapperMovies(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public void saveMovies() throws IOException {
        Files.list(Path.of(this.pathMovies)).forEach((path) -> {
            FileManagement fileManagement = new FileManagement(path);
            Movie movie = new Movie(fileManagement.getUri(this.baseHost), fileManagement.getTitle(),
                    fileManagement.getDescription(), fileManagement.getDuration());
            this.movieRepository.save(movie);
        });
    }
}
