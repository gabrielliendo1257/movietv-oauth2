package com.guille.media.reproductor.powercine.repository;

import com.guille.media.reproductor.powercine.models.GenreJpaEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends CrudRepository<GenreJpaEntity, Long>
{
    Optional<GenreJpaEntity> findByName(String name);
}
