package com.guille.media.reproductor.powercine.auth.domain.models;

import java.util.List;

public record DevTokenRequest(
        Integer userId,
        String username,
        List<String> roles
)
{
}
