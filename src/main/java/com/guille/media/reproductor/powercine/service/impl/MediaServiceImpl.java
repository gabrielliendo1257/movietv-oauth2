package com.guille.media.reproductor.powercine.service.impl;

import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import com.guille.media.reproductor.powercine.repository.MediaRepository;
import com.guille.media.reproductor.powercine.service.interfaces.IMediaService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MediaServiceImpl implements IMediaService {

  private final MediaRepository mediaRepository;

  public MediaServiceImpl(MediaRepository mediaRepository) {
    this.mediaRepository = mediaRepository;
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
}
