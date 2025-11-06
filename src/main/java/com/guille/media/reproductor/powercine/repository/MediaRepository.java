package com.guille.media.reproductor.powercine.repository;

import java.util.List;
import java.util.Optional;

import com.guille.media.reproductor.powercine.models.MediaJpaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaRepository extends JpaRepository<MediaJpaEntity, Integer> {
    Optional<MediaJpaEntity> findByTitle(String title);

    @Query("select u from MediaJpaEntity u where u.title like %?#{escape([0])}% escape ?#{escapeCharacter()}")
    List<MediaJpaEntity> findByLikeTitle(String title);
}
