package com.guille.media.reproductor.powercine.storage.infrastructure.providers.database.storage;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class StorageMetadataEmbeddable {
	@Column(nullable = false, length = 10)
	private String contentType;

	@Column(nullable = false, length = 10)
	private long contentLength;

	@Column(nullable = false, length = 30)
	private String checksum;

	@Column(nullable = false, length = 10)
	private String lastModifiedAt;
}
