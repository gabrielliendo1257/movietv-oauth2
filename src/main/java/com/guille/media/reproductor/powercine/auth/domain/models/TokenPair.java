package com.guille.media.reproductor.powercine.auth.domain.models;

import java.time.Instant;

public record TokenPair(
        String accessToken,
        String refreshToken,
        Instant accessTokenExpiration,
        Instant refreshTokenExpiration
)
{
}
