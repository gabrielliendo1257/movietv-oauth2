package com.guille.media.reproductor.powercine.storage.infrastructure.errors;


import com.guille.media.reproductor.powercine.storage.domain.vos.MimeType;

public class UnsupportedMimeTypeException extends RuntimeException
{
    private MimeType mimeType;

    public UnsupportedMimeTypeException(MimeType mimeType)
    {
        super();
        this.mimeType = mimeType;
    }
}
