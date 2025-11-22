package com.guille.media.reproductor.powercine.service.interfaces;


import io.minio.http.Method;

public interface S3Service {
    String getSignatureUrl(String bucket, String filename, Method method, Integer expire);

    Boolean existBucket(String bucket);

    void createBucket(String bucket);
}
