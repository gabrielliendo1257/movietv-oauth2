package com.guille.media.reproductor.powercine.service.impl;

import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.repository.MediaRepository;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class MediaServiceImpl implements IMediaService {

    private final MediaRepository mediaRepository;

    public MediaServiceImpl(MediaRepository mediaRepository) {
        this.mediaRepository = mediaRepository;
    }

    @Override
    public Optional<MediaJpaEntity> findByTitle(String title) {
        return this.mediaRepository.findByTitle(title);
    }

    @Override
    public Optional<MediaJpaEntity> findByid(Integer id) {
        return this.mediaRepository.findById(id);
    }

    @Override
    public List<MediaJpaEntity> findByLikeTitle(String likeTitle) {
        return this.mediaRepository.findByLikeTitle(likeTitle);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STANDARD_USER', 'PREMIUM_USER')")
    @Override
    public MediaJpaEntity save(MediaJpaEntity entity) {
        return this.mediaRepository.save(entity);
    }
}
