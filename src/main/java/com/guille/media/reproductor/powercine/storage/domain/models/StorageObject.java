package com.guille.media.reproductor.powercine.storage.domain.models;

import java.util.Objects;

import com.guille.media.reproductor.powercine.storage.domain.vos.StorageKey;
import com.guille.media.reproductor.powercine.storage.domain.vos.StorageLocation;
import com.guille.media.reproductor.powercine.storage.domain.vos.StorageMetadata;

import lombok.Getter;

/**
 * Representa un objeto almacenado lógicamente en el sistema.
 *
 * <p>Puede tratarse de:
 * <ul>
 *   <li>Una película.</li>
 *   <li>Un archivo de subtítulos.</li>
 *   <li>Una imagen de portada.</li>
 *   <li>Un documento adjunto.</li>
 * </ul>
 *
 * <p>Esta entidad une:
 * <ul>
 *   <li>La clave lógica del objeto ({@link StorageKey}).</li>
 *   <li>La ubicación física ({@link StorageLocation}).</li>
 *   <li>Los metadatos descriptivos ({@link StorageMetadata}).</li>
 * </ul>
 *
 * <p>El dominio trabaja con esta entidad sin depender del proveedor concreto
 * de almacenamiento.
 */
public final class StorageObject {

	@Getter
	private final String storageId;
	@Getter
	private final StorageLocation location;
	@Getter
	private final StorageMetadata metadata;
	// @Getter
	// private final Integer ownerId;
	public StorageStatus status;

	public enum StorageStatus {
		PROCESSING, DELETED, COMPLETED
	}

	public StorageObject(
		StorageLocation location,
		StorageMetadata metadata,
		String storageId,
		// Integer ownerId,
		StorageStatus status
	) {
		this.location = Objects.requireNonNull(location);
		this.metadata = metadata;
		this.storageId = Objects.requireNonNull(storageId);
		// this.ownerId = Objects.requireNonNull(ownerId);
		this.status = Objects.requireNonNull(status);
	}

	/**
	 * @return tamaño del objeto en bytes.
	 */
	public long sizeInBytes() {
		return metadata.contentLength();
	}

	/**
	 * @return tipo MIME del objeto.
	 */
	public String contentType() {
		return metadata.contentType();
	}

	/**
	 * Determina si el objeto representa un video.
	 *
	 * @return true si el tipo MIME es de video.
	 */
	public boolean isVideo() {
		return metadata.isVideo();
	}

	/**
	 * Determina si el objeto representa una imagen.
	 *
	 * @return true si el tipo MIME es de imagen.
	 */
	public boolean isImage() {
		return metadata.isImage();
	}

	/**
	 * Determina si el objeto representa un archivo de subtítulos.
	 *
	 * @return true si el tipo MIME corresponde a subtítulos.
	 */
	public boolean isSubtitle() {
		return metadata.isSubtitle();
	}

	public boolean isAvailable() {
		return this.status == StorageStatus.COMPLETED;
	}
}
