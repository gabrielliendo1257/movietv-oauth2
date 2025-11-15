package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.exceptions.EntityPersistenceException;
import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import com.guille.media.reproductor.powercine.repository.SignatureRepository;
import com.guille.media.reproductor.powercine.service.interfaces.MediaSignatureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MediaSignatureServiceImpl implements MediaSignatureService {

    private final SignatureRepository signatureRepository;

    public MediaSignatureServiceImpl(SignatureRepository signatureRepository) {
        this.signatureRepository = signatureRepository;
    }

    @Override
    public MediaJpaSignature save(MediaJpaSignature mediaJpaSignature) {
        try {
            return this.signatureRepository.save(mediaJpaSignature);
        } catch (Exception e) {
            log.info("MediaSignatureServiceImpl save(MediaJpaSignature mediaJpaSignature) error", e);
            throw new EntityPersistenceException("Error persist entity signature.");
        }
    }
}
