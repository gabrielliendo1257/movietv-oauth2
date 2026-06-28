package com.guille.media.reproductor.powercine.storage.app.commands.response;

import com.guille.media.reproductor.powercine.storage.domain.vos.StorageKey;
import java.time.Instant;

public record StreamingSession(
	String uploadId,
	String streamingUrl,
	StorageKey storageKey,
	Instant expiresAt,
	String method
) {

}
