package com.guille.media.reproductor.powercine.service.interfaces;

import com.guille.media.reproductor.powercine.dto.response.JwtAccessToken;

public interface OAuthService {
    JwtAccessToken getAccessToken(String code);
}
