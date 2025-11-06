package com.guille.media.reproductor.powercine.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.models.MediaJpaEntity;

public interface IMediaService {

    Optional<MediaJpaEntity> findByTitle(String title);

    Optional<MediaJpaEntity> findByid(Integer id);

    List<MediaJpaEntity> findByLikeTitle(String likeTitle);

    MediaJpaEntity save(MediaJpaEntity entity);
}
