package com.guille.media.reproductor.powercine.storage.app.commands.response;

import com.guille.media.reproductor.powercine.storage.domain.vos.StorageKey;
import java.time.Instant;
import java.util.Map;

public record UploadSession(
	String uploadId,
	String uploadUrl,
	StorageKey storageKey,
	Instant expiresAt,
	Map<String, String> headers,
	String method
) {

}