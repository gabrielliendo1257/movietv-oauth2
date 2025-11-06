package com.guille.media.reproductor.powercine.restcontroller;

import com.guille.media.reproductor.powercine.dto.request.MediaDto;
import com.guille.media.reproductor.powercine.mapper.MediaMapper;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "${api.path.base}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    private final IMediaService mediaService;
    private final MediaMapper mediaMapper;

    public MovieController(IMediaService mediaService, MediaMapper mediaMapper) {
        this.mediaService = mediaService;
        this.mediaMapper = mediaMapper;
    }

    @GetMapping(value = "/hello")
    public ResponseEntity<?> hello() {
        return new ResponseEntity<String>("Hello STANDARD_CONTENT.", HttpStatus.OK);
    }

    @Secured("hasRole('ADMIN')")
    @GetMapping(value = "/content")
    public ResponseEntity<?> content() {
        return new ResponseEntity<String>("Hello admin.", HttpStatus.OK);
    }

    @PostMapping(value = "/post_content")
    public String posted() {
        return "Content posted succesfully";
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> processPersitMovie(@RequestBody MediaDto mediaDto) {
        MediaJpaEntity entity = this.mediaMapper.toEntity(mediaDto);

        return ResponseEntity.ok(this.mediaService.save(entity));
    }

}
