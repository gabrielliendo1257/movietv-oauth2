package com.guille.media.reproductor.powercine.storage.domain.models;

import java.time.Duration;

public record UploadConfiguration(
	Duration expiration,
	UploadType uploadType,
	Long chunkSize
) {

}
