package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.exceptions.media.MediaAlreadyExistException;
import com.guille.media.reproductor.powercine.models.MediaJpaSignature;
import com.guille.media.reproductor.powercine.repository.SignatureRepository;
import com.guille.media.reproductor.powercine.service.interfaces.MediaSignatureService;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Profile(value = {"test"})
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
            throw new MediaAlreadyExistException("Error persist entity signature.");
        }
    }
}
