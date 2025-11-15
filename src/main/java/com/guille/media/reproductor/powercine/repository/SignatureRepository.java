package com.guille.media.reproductor.powercine.repository;

import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignatureRepository extends JpaRepository<MediaJpaSignature, String> {
}
