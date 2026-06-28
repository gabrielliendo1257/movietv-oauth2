package com.guille.media.reproductor.powercine.storage.domain.exceptions;

public class StorageException extends RuntimeException
{

    public StorageException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public StorageException(String message)
    {
        super(message);
    }
}
