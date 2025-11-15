package com.guille.media.reproductor.powercine.models.listeners;

import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import com.guille.media.reproductor.powercine.utils.enums.StatusMedia;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDate;

public class SignatureListener {

    @PrePersist
    public void setId(MediaJpaSignature mediaJpaSignature) {
        mediaJpaSignature.setStatus(StatusMedia.PROCESSING);
        mediaJpaSignature.setUploadAt(LocalDate.now());
    }

    @PreUpdate
    public void setUpdatedAt(MediaJpaSignature mediaJpaSignature) {
        mediaJpaSignature.setUpdatedAt(LocalDate.now());
    }
}
