package com.guille.media.reproductor.powercine.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.dto.request.CreateMediaRequest;
import com.guille.media.reproductor.powercine.models.MediaJpaEntity;
import io.minio.http.Method;

public interface IMediaService {

    Optional<MediaJpaEntity> findByTitle(String title);

    Optional<MediaJpaEntity> findByid(Integer id);

    List<MediaJpaEntity> findByLikeTitle(String likeTitle);

    MediaJpaEntity save(MediaJpaEntity entity);

    List<MediaJpaEntity> findAllMedias();

    String getPresignedUrl(String bucket, String filename, Method method, Integer expire);

    void createMedia(CreateMediaRequest request);
}
