package com.guille.media.reproductor.powercine.storage.domain.vos;


import java.util.concurrent.TimeUnit;

public record PresignedUrl(
        String url,
        int expire,
        TimeUnit timeUnit,
        Object httpMethod
)
{
}
