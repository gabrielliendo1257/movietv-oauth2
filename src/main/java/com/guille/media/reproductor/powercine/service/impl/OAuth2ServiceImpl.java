package com.guille.media.reproductor.powercine.service.impl;

import com.guille.media.reproductor.powercine.models.JwtAccessToken;
import com.guille.media.reproductor.powercine.service.interfaces.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class OAuth2ServiceImpl implements OAuthService {

    @Value("${powercine.env.oauth2.redirect}")
    private String oAuth2Redirect;

    private final RestTemplate restTemplate;

    public OAuth2ServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public JwtAccessToken getAccessToken(String code) {
        log.info("Authentication exchange code: {}", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("app-movie", "super-secret");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("grant_type", "authorization_code");
        map.add("redirect_uri", this.oAuth2Redirect);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        JwtAccessToken response = this.restTemplate.exchange("http://localhost:8080/oauth2/token", HttpMethod.POST, request, JwtAccessToken.class).getBody();
        log.info("Authentication response: {}", response);

        return response;
    }
}
