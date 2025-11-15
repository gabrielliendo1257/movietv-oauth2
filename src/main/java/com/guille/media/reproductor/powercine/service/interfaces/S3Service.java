package com.guille.media.reproductor.powercine.service.interfaces;


public interface S3Service {
    String getSignatureUrl(String bucket, String filename);

    Boolean existBucket(String bucket);

    void createBucket(String bucket);
}
