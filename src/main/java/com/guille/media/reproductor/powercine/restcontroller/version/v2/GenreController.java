package com.guille.media.reproductor.powercine.restcontroller.version.v2;

import com.guille.media.reproductor.powercine.service.interfaces.GenreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

public class GenreController
{
    private final GenreService genreService;

    public GenreController(GenreService genreService)
    {
        this.genreService = genreService;
    }

}
