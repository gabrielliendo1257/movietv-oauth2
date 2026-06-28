package com.guille.media.reproductor.powercine.restcontroller.version.v1;

import com.guille.media.reproductor.powercine.dto.request.CreateMediaRequest;
import com.guille.media.reproductor.powercine.dto.request.FileUploadDto;
import com.guille.media.reproductor.powercine.dto.response.MediaSignatureDto;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.pipes.FilenameConvert;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@CrossOrigin(value = "${powercine.env.frontendapp.endpoint}", methods = {RequestMethod.GET, RequestMethod.POST})
//@RestController
//@RequestMapping(value = "${api.path.base}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    private final IMediaService mediaService;
    private final FilenameConvert filenameConvert;

    private static final String MINIO_DEFAULT_BUCKET = "default";

    public MovieController(IMediaService mediaService, FilenameConvert filenameConvert) {
        this.mediaService = mediaService;
        this.filenameConvert = filenameConvert;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllMovies() {
        List<MediaJpaEntity> medias = this.mediaService.findAllMedias();
        return medias.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(medias);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveMedia(@RequestBody CreateMediaRequest createMediaRequest) {
        try {
            log.info("Request controller: {}",  createMediaRequest);
            this.mediaService.createMedia(createMediaRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/upload-session")
    public ResponseEntity<?> uploadSession(@RequestParam(required = false) Boolean complete, @RequestBody FileUploadDto upload) {
        if (upload.filename().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Filename is empty."));
        }

        if (complete != null && complete) {
            log.info("Session upload complete");
            return ResponseEntity.ok(Map.of("message", "Session upload complete"));
        }

        String newFilename = this.filenameConvert.convert(upload.filename());
        String urlSignature = this.mediaService.getPresignedUrl(MINIO_DEFAULT_BUCKET, newFilename, Method.PUT, 5);

        return ResponseEntity.ok(new MediaSignatureDto(urlSignature, newFilename));
    }

    @PostMapping(value = "/streaming-session")
    public ResponseEntity<?> presignedMediaStreaming(@RequestBody FileUploadDto upload) {
        log.info("Request controller: {}",  upload);
        if (upload.filename().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "Filename is empty."));
        }

        String urlPresigned = this.mediaService.getPresignedUrl(MINIO_DEFAULT_BUCKET, upload.filename(), Method.GET, 15);

        return ResponseEntity.ok(new MediaSignatureDto(urlPresigned, upload.filename()));
    }

    @MessageMapping("/up-file")
    @SendTo("/topic/public")
    public String getMedia(@Payload FileUploadDto file, SimpMessageHeaderAccessor headerAccessor)
    {
        log.info("Request websocket controller: {}",  file);
        headerAccessor.getSessionAttributes().put("filename", file.filename());

        return "File uploading: " + file.filename();
    }
}
