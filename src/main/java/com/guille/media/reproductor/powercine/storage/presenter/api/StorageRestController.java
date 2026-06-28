package com.guille.media.reproductor.powercine.storage.presenter.api;

import com.guille.media.reproductor.powercine.storage.domain.service.StorageService;
import com.guille.media.reproductor.powercine.storage.presenter.dto.request.StreamingRequest;
import com.guille.media.reproductor.powercine.storage.presenter.dto.request.UploadRequest;
import com.guille.media.reproductor.powercine.storage.presenter.mapper.UploadMapper;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/movie/storage", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class StorageRestController {

	private final StorageService storageService;
	private final UploadMapper uploadMapper;

	public StorageRestController(StorageService storageService, UploadMapper uploadMapper) {
		this.storageService = storageService;
		this.uploadMapper = uploadMapper;
	}

	@PostMapping(value = "/upload")
	public ResponseEntity<?> uploadSession(@Valid @RequestBody UploadRequest uploadRequest) {
		return ResponseEntity.ok(
			this.uploadMapper.toUploadResponse(
				this.storageService.createUploadSession(
					this.uploadMapper.toUploadCommand(uploadRequest)
				)
			)
		);
	}

	@PostMapping(value = "/streaming")
	public ResponseEntity<?> streaming(@Valid @RequestBody StreamingRequest streamingRequest) {
		return ResponseEntity.ok(
			this.uploadMapper.toStreamingSessionResponse(
				this.storageService.generateStreamingSession(
					this.uploadMapper.toStreamingCommand(streamingRequest)
				)
			)
		);
	}

	@PostMapping(value = "/upload/{uploadId}/complete")
	public ResponseEntity<?> completeUpload(@PathVariable String uploadId) {
		this.storageService.completeUpload(uploadId);
		return ResponseEntity.ok().build();
	}
}
