package com.guille.media.reproductor.powercine.restcontroller.version.v2;

import com.guille.media.reproductor.powercine.dto.request.CreateMediaRequest;
import com.guille.media.reproductor.powercine.dto.request.FileUploadDto;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;
import io.minio.http.Method;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@CrossOrigin(
        value = "${powercine.env.frontendapp.endpoint}",
        methods = {RequestMethod.GET, RequestMethod.POST}
)
//@RestController
/*@RequestMapping(
        value = "${api.v2.path.base}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)*/
public class MovieController
{

    private final IMediaService mediaService;

    private static final String MINIO_DEFAULT_BUCKET = "default";

    public MovieController(IMediaService mediaService)
    {
        this.mediaService = mediaService;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllMovies()
    {
        return ResponseEntity.ok(this.mediaService.findAllMedias());
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveMedia(@RequestBody CreateMediaRequest createMediaRequest)
    {
        log.info("Media persist: {}", createMediaRequest);
        this.mediaService.createMedia(createMediaRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping(value = "/upload-session")
    public ResponseEntity<?> uploadSession(@Valid @RequestBody FileUploadDto upload)
    {
        return ResponseEntity.ok(this.mediaService.getMediaSignature(
                MINIO_DEFAULT_BUCKET,
                upload,
                Method.PUT,
                5,
                true));
    }

    @PostMapping(value = "/streaming-session")
    public ResponseEntity<?> presignedMediaStreaming(@RequestBody FileUploadDto upload)
    {
        log.info("File to upload (v2): {}", upload);
        return ResponseEntity.ok(this.mediaService.getMediaSignature(
                MINIO_DEFAULT_BUCKET,
                upload,
                Method.GET,
                15,
                false));
    }
}
