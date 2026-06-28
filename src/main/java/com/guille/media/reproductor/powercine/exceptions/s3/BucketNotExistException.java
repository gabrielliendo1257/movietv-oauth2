package com.guille.media.reproductor.powercine.exceptions.s3;

public class BucketNotExistException extends RuntimeException {

    public BucketNotExistException(String message) {
        super(message);
    }
}
