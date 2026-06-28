package com.guille.media.reproductor.powercine.storage.domain.service;

import com.guille.media.reproductor.powercine.storage.domain.models.UploadConfiguration;
import com.guille.media.reproductor.powercine.storage.domain.vos.MimeType;

public interface UploadPolicy
{
    UploadConfiguration resolve(long size, MimeType mimeType);

    long maxUploadSize();

    boolean supports(MimeType mimeType);
}
