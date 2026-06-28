package com.guille.media.reproductor.powercine.storage.infrastructure.providers.database.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageDataRepository extends JpaRepository<StorageJpaEntity, String> {

}
