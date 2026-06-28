package com.guille.media.reproductor.powercine.auth.domain.models;

import java.util.List;

public record UserIdentity(
        Integer userId,
        String username,
        List<String> roles
)
{
}
