package com.guille.media.reproductor.powercine.service.impl;


import com.guille.media.reproductor.powercine.exceptions.s3.GetPresignedObjectException;
import com.guille.media.reproductor.powercine.exceptions.s3.MakeBucketException;
import com.guille.media.reproductor.powercine.service.interfaces.S3Service;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@Profile(value = {"test"})
public class S3ServiceImpl implements S3Service {

    private final MinioClient minioClient;

    public S3ServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public String getSignatureUrl(String bucket, String filename, Method method, Integer expire) {
        try {
            String presignedUrl = this.minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(method)
                            .bucket(bucket)
                            .object(filename)
                            .expiry(expire, TimeUnit.MINUTES)
                            .build()
            );

            log.info("Presigned presignedUrl: {}", presignedUrl);
            return presignedUrl;

        } catch (Exception e) {
            log.info("Error getting signature presignedUrl: {}", e.getMessage());
            throw new GetPresignedObjectException("Error getting signature presignedUrl: " + e.getMessage());
        }
    }

    @Override
    public Boolean existBucket(String bucket) {
        try {
            return this.minioClient.bucketExists(BucketExistsArgs
                    .builder()
                    .bucket(bucket)
                    .build()
            );
        } catch (Exception ex) {
            log.info("Error getting bucket: {}", ex.getMessage());

            return false;
        }

    }

    @Override
    public void createBucket(String bucket) {
        try {
            this.minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(bucket)
                    .build());
        } catch (Exception ex) {
            log.info("Error creating bucket: {}", ex.getMessage());

            throw new MakeBucketException("Error creating bucket " + bucket);
        }

    }
}
