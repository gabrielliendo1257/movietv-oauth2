package com.guille.media.reproductor.powercine.storage.domain.exceptions;

import com.guille.media.reproductor.powercine.storage.domain.vos.BucketName;

public class BucketNotFoundException extends RuntimeException
{
    public BucketNotFoundException(String message)
    {
        super(message);
    }
}
