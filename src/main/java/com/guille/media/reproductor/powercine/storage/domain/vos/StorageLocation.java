package com.guille.media.reproductor.powercine.storage.domain.vos;

public record StorageLocation(
	BucketName bucket,
	StorageKey storageKey
) {

}
