package com.guille.media.reproductor.powercine.restcontroller;

import java.util.List;
import java.util.Map;

import com.guille.media.reproductor.powercine.dto.request.AuthCode;
import com.guille.media.reproductor.powercine.dto.request.FileUploadDto;
import com.guille.media.reproductor.powercine.mapper.MediaMapper;
import com.guille.media.reproductor.powercine.models.JwtAccessToken;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;

import com.guille.media.reproductor.powercine.service.interfaces.OAuthService;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@CrossOrigin(value = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping(value = "${api.path.base}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    private final IMediaService mediaService;
    private final MediaMapper mediaMapper;

    private static final String MINIO_DEFAULT_BUCKET = "default";

    public MovieController(IMediaService mediaService, MediaMapper mediaMapper) {
        this.mediaService = mediaService;
        this.mediaMapper = mediaMapper;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllMovies() {
        List<MediaJpaEntity> medias = this.mediaService.findAllMedias();
        return medias.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(medias);
    }

    @PostMapping(value = "/upload-session")
    public ResponseEntity<?> uploadSession(@RequestParam(required = false) Boolean complete, @RequestBody FileUploadDto upload) {
        if (complete != null && complete) {
            log.info("Session upload complete");
        }

        MediaJpaSignature mediaJpaSignature = this.mediaService.getPresignedUrl(MINIO_DEFAULT_BUCKET, upload.filename());
        log.info("Entity media signature: {}", mediaJpaSignature);

        return ResponseEntity.ok(this.mediaMapper.toSignatureDto(mediaJpaSignature));
    }

}
