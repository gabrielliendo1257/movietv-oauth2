package com.guille.media.reproductor.powercine.storage.infrastructure.providers.database.storage;

import com.guille.media.reproductor.powercine.storage.domain.models.StorageObject;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StorageMapper {

    @Mapping(target = "bucketName", source = "location.bucket.bucketName")
    @Mapping(target = "objectKey", source = "location.storageKey.key")

    @Mapping(target = "contentType", source = "metadata.contentType")
    @Mapping(target = "contentLength", source = "metadata.contentLength")
    @Mapping(target = "checksum", source = "metadata.checksum")
    @Mapping(target = "lastModifiedAt", source = "metadata.lastModifiedAt")
    @Mapping(target = "attributes", source = "metadata.attributes")
    StorageJpaEntity toJpaEntity(StorageObject storageObject);

    @Mapping(target = "location", expression = """
                java(new StorageLocation(
                    BucketName.of(entity.getBucketName()),
                    new StorageKey(entity.getObjectKey())
                ))
            """)
    @Mapping(target = "metadata", expression = """
                java(new StorageMetadata(
                    entity.getContentType(),
                    entity.getContentLength(),
                    entity.getChecksum(),
                    entity.getLastModifiedAt(),
                    entity.getAttributes()
                ))
            """)
    StorageObject toStorageObject(StorageJpaEntity entity);
}
