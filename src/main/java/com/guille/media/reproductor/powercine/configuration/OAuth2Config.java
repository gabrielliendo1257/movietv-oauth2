package com.guille.media.reproductor.powercine.configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

import com.guille.media.reproductor.powercine.auth.config.OAuth2SecurityRules;
import com.guille.media.reproductor.powercine.auth.presenter.filters.DevAuthenticationFilter;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @Configuration
public class OAuth2Config {

    @Value("${powercine.env.oauth2.redirect}")
    private String oauth2Redirect;

    private OAuth2SecurityRules oAuth2SecurityRules;
    private DevAuthenticationFilter devAuthenticationFilter;

    private static KeyPair generateRsaKey() {

        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return keyPair;
    }

    // @Bean
    // @Order(value = Ordered.HIGHEST_PRECEDENCE)
    // SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws
    // Exception {
    // log.info("oauth2SecurityFilterChain() executing");
    // OAuth2AuthorizationServerConfigurer oAuthorizationServerConfigurer =
    // OAuth2AuthorizationServerConfigurer
    // .authorizationServer();
    //
    // return http
    // .securityMatcher(oAuthorizationServerConfigurer.getEndpointsMatcher())
    // .exceptionHandling(exceptionConfig -> exceptionConfig
    // .defaultAuthenticationEntryPointFor(new
    // LoginUrlAuthenticationEntryPoint("/login"),
    // new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
    // .with(oAuthorizationServerConfigurer, (authorizationServer) ->
    // authorizationServer
    // .oidc(Customizer.withDefaults()))
    // .authorizeHttpRequests((authorizeConfig) -> {
    // authorizeConfig
    // .anyRequest().authenticated();
    // })
    // .build();
    // }

    @Bean
    BearerTokenResolver bearerTokenResolver() {
        log.info("Bearer token resolver");
        return request -> {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) {
                return null;
            }

            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName())) {
                    log.info("Access token: {}", c.getValue());
                    return c.getValue();
                }
            }
            return null;
        };
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();

        RSAKey rsaKey = new RSAKey.Builder(rsaPublicKey)
                .privateKey(rsaPrivateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return null;
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }
}
