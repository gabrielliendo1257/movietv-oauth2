package com.guille.media.reproductor.powercine.storage.infrastructure.providers.database.storage;

import java.time.Instant;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.guille.media.reproductor.powercine.storage.app.converters.MetadataAttributesConverter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Table(name = "storage")
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StorageJpaEntity {

	@Id
	@Column(unique = true, nullable = false, updatable = false, length = 10)
	private String storageId;

	// @Column(nullable = false, updatable = false, length = 5)
	// private Integer ownerId;

	@Column(unique = true, nullable = false, length = 50)
	private String objectKey;

	@Column(nullable = false, length = 30)
	private String bucketName;

	@Column(nullable = false, length = 10)
	private String status;

	private String contentType;

	private Long contentLength;

	private String checksum;

	private Instant lastModifiedAt;

	@Column(columnDefinition = "jsonb")
	@Convert(converter = MetadataAttributesConverter.class)
	private Map<String, String> attributes;
}
