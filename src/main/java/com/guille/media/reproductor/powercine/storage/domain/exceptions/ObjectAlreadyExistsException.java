package com.guille.media.reproductor.powercine.storage.domain.exceptions;

import com.guille.media.reproductor.powercine.storage.domain.vos.StorageKey;

public class ObjectAlreadyExistsException extends RuntimeException
{
    public StorageKey key;

    public ObjectAlreadyExistsException(StorageKey key)
    {
        super();
        this.key = key;
    }
}
