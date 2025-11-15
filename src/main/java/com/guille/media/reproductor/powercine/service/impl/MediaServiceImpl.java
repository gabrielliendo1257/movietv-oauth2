package com.guille.media.reproductor.powercine.service.impl;

import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.exceptions.BucketNotExistException;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import com.guille.media.reproductor.powercine.pipes.FilenameConvert;
import com.guille.media.reproductor.powercine.repository.MediaRepository;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;

import com.guille.media.reproductor.powercine.service.interfaces.MediaSignatureService;
import com.guille.media.reproductor.powercine.service.interfaces.S3Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaServiceImpl implements IMediaService {

    private final MediaRepository mediaRepository;
    private final MediaSignatureService mediaSignatureService;
    private final S3Service s3Service;
    private final FilenameConvert filenameConvert;

    public MediaServiceImpl(MediaRepository mediaRepository, MediaSignatureService mediaSignatureService, S3Service s3Service, FilenameConvert filenameConvert) {
        this.mediaRepository = mediaRepository;
        this.mediaSignatureService = mediaSignatureService;
        this.s3Service = s3Service;
        this.filenameConvert = filenameConvert;
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

    @PreAuthorize("hasAnyRole('ADMIN', 'PREMIUM_USER')")
    @Override
    public MediaJpaSignature getPresignedUrl(String bucket, String filename) {
        String newFilename = this.filenameConvert.convert(filename);

        if (!this.s3Service.existBucket(bucket)) {
            throw new BucketNotExistException("Bucket " + bucket + " not exist");
        }

        String urlSignature = this.s3Service.getSignatureUrl(bucket, newFilename);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return this.mediaSignatureService.save(new MediaJpaSignature(authentication.getName(), urlSignature));
    }

}
