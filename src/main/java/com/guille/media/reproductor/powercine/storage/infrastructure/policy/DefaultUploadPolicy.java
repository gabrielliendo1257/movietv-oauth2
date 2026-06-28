package com.guille.media.reproductor.powercine.storage.infrastructure.policy;

import com.guille.media.reproductor.powercine.storage.app.errors.UploadSizeExceededException;
import com.guille.media.reproductor.powercine.storage.domain.models.UploadConfiguration;
import com.guille.media.reproductor.powercine.storage.domain.models.UploadType;
import com.guille.media.reproductor.powercine.storage.domain.service.UploadPolicy;
import com.guille.media.reproductor.powercine.storage.domain.vos.MimeType;
import com.guille.media.reproductor.powercine.storage.infrastructure.errors.UnsupportedMimeTypeException;
import java.time.Duration;
import org.springframework.stereotype.Service;

@Service
public class DefaultUploadPolicy implements UploadPolicy {

	private static final long KB = 1024L;
	private static final long MB = KB * 1024L;
	private static final long GB = MB * 1024L;

	private static final long MAX_UPLOAD_SIZE = 10L * GB;

	private static final long MULTIPART_THRESHOLD = 500L * MB;

	@Override
	public UploadConfiguration resolve(long size, MimeType mimeType) {
		validateSize(size);
		validateMimeType(mimeType);

		if (size >= MULTIPART_THRESHOLD) {
			return new UploadConfiguration(
				Duration.ofHours(6),
				UploadType.MULTIPART,
				50L * MB
			);
		}

		return new UploadConfiguration(
			Duration.ofMinutes(30),
			UploadType.SIMPLE,
			null
		);
	}

	@Override
	public long maxUploadSize() {
		return MAX_UPLOAD_SIZE;
	}

	@Override
	public boolean supports(MimeType mimeType) {
		return switch (mimeType.value()) {
			case "image/png",
				 "image/jpeg",
				 "video/mp4",
				 "video/x-matroska",
				 "application/pdf" -> true;

			default -> false;
		};
	}

	private void validateSize(long size) {
		if (size <= 0) {
			throw new IllegalArgumentException(
				"Upload size must be greater than zero"
			);
		}

		if (size > MAX_UPLOAD_SIZE) {
			throw new UploadSizeExceededException(
				size,
				MAX_UPLOAD_SIZE
			);
		}
	}

	private void validateMimeType(MimeType mimeType) {
		if (!supports(mimeType)) {
			throw new UnsupportedMimeTypeException(
				mimeType
			);
		}
	}
}
