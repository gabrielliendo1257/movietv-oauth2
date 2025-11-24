package com.guille.media.reproductor.powercine.service.impl;

import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.dto.request.CreateMediaRequest;
import com.guille.media.reproductor.powercine.exceptions.BucketNotExistException;
import com.guille.media.reproductor.powercine.mapper.MediaMapper;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import com.guille.media.reproductor.powercine.repository.MediaRepository;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;

import com.guille.media.reproductor.powercine.service.interfaces.S3Service;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class MediaServiceImpl implements IMediaService {

    private final MediaRepository mediaRepository;
    private final S3Service s3Service;
    private final MediaMapper mediaMapper;

    public MediaServiceImpl(MediaRepository mediaRepository, S3Service s3Service, MediaMapper mediaMapper) {
        this.mediaRepository = mediaRepository;
        this.s3Service = s3Service;
        this.mediaMapper = mediaMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<MediaJpaEntity> findByTitle(String title) {
        return this.mediaRepository.findByTitle(title);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<MediaJpaEntity> findByid(Integer id) {
        return this.mediaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MediaJpaEntity> findByLikeTitle(String likeTitle) {
        return this.mediaRepository.findByLikeTitle(likeTitle);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STANDARD_USER', 'PREMIUM_USER') or hasAnyAuthority('PLATFORM_MANAGER', 'ADMININTRATOR')")
    @Transactional
    @Override
    public MediaJpaEntity save(MediaJpaEntity entity) {
        return this.mediaRepository.save(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public List<MediaJpaEntity> findAllMedias() {
        return this.mediaRepository.findAll();
    }

    @Override
    public String getPresignedUrl(String bucket, String filename, Method method, Integer expire) {
        if (!this.s3Service.existBucket(bucket)) {
            throw new BucketNotExistException("Bucket " + bucket + " not exist");
        }

        return this.s3Service.getSignatureUrl(bucket, filename, method, expire);
    }

    @Override
    public void createMedia(CreateMediaRequest request) {
        MediaJpaEntity mediaJpaEntity = this.mediaMapper.toEntity(request.media());
        mediaJpaEntity.addMediaSignature(new MediaJpaSignature(request.file().filename()));
        log.info("MediaJpaEntity: {}", mediaJpaEntity);

        this.mediaRepository.save(mediaJpaEntity);
    }

}
