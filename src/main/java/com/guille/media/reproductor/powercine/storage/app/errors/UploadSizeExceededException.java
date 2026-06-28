package com.guille.media.reproductor.powercine.storage.app.errors;

public class UploadSizeExceededException extends RuntimeException
{
    private long size;
    private long maxSize;

    public UploadSizeExceededException(long size, long maxSize)
    {
        super();
        this.size = size;
        this.maxSize = maxSize;
    }
}
