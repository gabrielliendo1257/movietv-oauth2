package com.guille.media.reproductor.powercine.storage.domain.vos;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

import java.time.Duration;

@Data
public class PresignedUploadRequest
{
    private Duration expiration;
    private Map<String, String> headers = new HashMap<>();

    public PresignedUploadRequest(Duration expiration, Map<String, String> headers) {
        this.expiration = expiration;
        this.headers = headers;
    }
}
