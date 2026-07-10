package com.guille.media.reproductor.powercine.storage.app.usecases;

import java.time.Instant;
import java.util.Map;

import com.guille.media.reproductor.powercine.storage.app.commands.requets.CreateUploadCommand;
import com.guille.media.reproductor.powercine.storage.app.commands.requets.StreamingCommand;
import com.guille.media.reproductor.powercine.storage.app.commands.response.StreamingSession;
import com.guille.media.reproductor.powercine.storage.app.commands.response.UploadSession;
import com.guille.media.reproductor.powercine.storage.domain.exceptions.BucketNotFoundException;
import com.guille.media.reproductor.powercine.storage.domain.exceptions.ObjectAlreadyExistsException;
import com.guille.media.reproductor.powercine.storage.domain.exceptions.StorageObjectNotAvailable;
import com.guille.media.reproductor.powercine.storage.domain.models.StorageKeyGenerator;
import com.guille.media.reproductor.powercine.storage.domain.models.StorageObject;
import com.guille.media.reproductor.powercine.storage.domain.models.StorageObject.StorageStatus;
import com.guille.media.reproductor.powercine.storage.domain.models.UploadConfiguration;
import com.guille.media.reproductor.powercine.storage.domain.ports.ObjectStorageService;
import com.guille.media.reproductor.powercine.storage.domain.ports.StorageRepository;
import com.guille.media.reproductor.powercine.storage.domain.service.StorageService;
import com.guille.media.reproductor.powercine.storage.domain.service.UploadPolicy;
import com.guille.media.reproductor.powercine.storage.domain.vos.BucketName;
import com.guille.media.reproductor.powercine.storage.domain.vos.MimeType;
import com.guille.media.reproductor.powercine.storage.domain.vos.PermissionUrl;
import com.guille.media.reproductor.powercine.storage.domain.vos.PresignedUploadRequest;
import com.guille.media.reproductor.powercine.storage.domain.vos.StorageKey;
import com.guille.media.reproductor.powercine.storage.domain.vos.StorageLocation;
import com.guille.media.reproductor.powercine.storage.domain.vos.StorageMetadata;
import com.guille.media.reproductor.powercine.storage.domain.vos.UploadId;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AppStorageService implements StorageService {

    private final ObjectStorageService objectStoragePort;
    private final StorageKeyGenerator storageKeyGenerator;
    private final UploadPolicy uploadPolicy;
    private final StorageRepository storageRepository;

    public AppStorageService(
            ObjectStorageService objectStorageService, StorageKeyGenerator storageKeyGenerator,
            UploadPolicy uploadPolicy, StorageRepository storageRepository) {
        this.objectStoragePort = objectStorageService;
        this.storageKeyGenerator = storageKeyGenerator;
        this.uploadPolicy = uploadPolicy;
        this.storageRepository = storageRepository;
    }

    @Override
    public UploadSession createUploadSession(CreateUploadCommand createUploadCommand) {
        BucketName bucket = BucketName.of("users"); // TODO No hard codear el nombre
        log.info("Starting upload session: {}", createUploadCommand);

        if (!this.objectStoragePort.bucketExists(bucket)) {
            throw new BucketNotFoundException("Bucket not found: " + bucket.bucketName());
        }

        StorageKey key = storageKeyGenerator.generate();
        StorageLocation location = new StorageLocation(bucket, key);
        if (this.objectStoragePort.objectExists(location)) {
            throw new ObjectAlreadyExistsException(key);
        }
        UploadConfiguration uploadConfiguration = this.uploadPolicy.resolve(
                createUploadCommand.size(), createUploadCommand.mimeType());
        PresignedUploadRequest request = new PresignedUploadRequest(
                uploadConfiguration.expiration(),
                Map.of("Content-Type", createUploadCommand.mimeType().value()));

        PermissionUrl uploadUrl = this.objectStoragePort.createUploadUrl(
                request, location);

        StorageMetadata storageMetadata = new StorageMetadata(
                createUploadCommand.mimeType().value(), createUploadCommand.size(), "test_checksum",
                Instant.now(),
                Map.of("test_atribute", "subtitles"));
        StorageObject storageObject = this.storageRepository.save(
                new StorageObject(
                        location, storageMetadata, UploadId.generate(),
                        StorageStatus.PROCESSING));

        UploadSession uploadSession = new UploadSession(
                storageObject.getStorageId(),
                uploadUrl.presignedUrl(),
                key,
                Instant.now().plus(uploadConfiguration.expiration()),
                uploadUrl.headers(),
                uploadUrl.method());
        log.info("Success upload session, returning: {}", uploadSession);
        return uploadSession;
    }

    @Override
    public StreamingSession generateStreamingSession(StreamingCommand command) {
        StorageObject storageObjectFromRepository = this.storageRepository.findById(command.objectId());
        if (!storageObjectFromRepository.isAvailable()) {
            throw new StorageObjectNotAvailable("Storage object not available: " + command.objectId());
        }
        UploadConfiguration uploadConfiguration = this.uploadPolicy.resolve(
                storageObjectFromRepository.sizeInBytes(),
                MimeType.of(storageObjectFromRepository.getMetadata().contentType()));
        PresignedUploadRequest request = new PresignedUploadRequest(
                uploadConfiguration.expiration(),
                null);
        PermissionUrl permissionUrl = this.objectStoragePort.createStreamingUrl(
                request, storageObjectFromRepository.getLocation());

        return new StreamingSession(
                storageObjectFromRepository.getStorageId(),
                permissionUrl.presignedUrl(),
                storageObjectFromRepository.getLocation().storageKey(),
                Instant.now().plus(uploadConfiguration.expiration()),
                permissionUrl.method());
    }

    @Override
    public void completeUpload(String uploadId) {
        StorageObject storageObject = this.storageRepository.findById(uploadId);

        if (!this.objectStoragePort.objectExists(storageObject.getLocation())) {
            throw new StorageObjectNotAvailable("Storage object not available: " + uploadId);
        }

        storageObject.status = StorageStatus.COMPLETED;
        log.info("Completed upload {}", storageObject);
        this.storageRepository.save(storageObject);
    }
}
