package com.guille.media.reproductor.powercine.configuration.oauth;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

@Slf4j
@Configuration
public class OAuth2Config {

    @Value("${powercine.env.oauth2.redirect}")
    private String oauth2Redirect;

    @Bean
    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain oauth2SecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer oAuthorizationServerConfigurer = OAuth2AuthorizationServerConfigurer
                .authorizationServer();

        return http
                .securityMatcher(oAuthorizationServerConfigurer.getEndpointsMatcher())
                .exceptionHandling(exceptionConfig -> exceptionConfig
                        .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
                .with(oAuthorizationServerConfigurer, (authorizationServer) -> authorizationServer
                        .oidc(Customizer.withDefaults()))
                .authorizeHttpRequests((authorizeConfig) -> authorizeConfig
                        .anyRequest().authenticated())
                .build();
    }

    @Bean
    @Profile(value = {"dev", "test"})
    RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        var registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("app-movie")
                .clientSecret(passwordEncoder.encode("super-secret"))
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.OPENID)
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri(this.oauth2Redirect)
                .postLogoutRedirectUri("http://localhost:9090/")
                .tokenSettings(TokenSettings.builder()
                        .reuseRefreshTokens(false)
                        .accessTokenTimeToLive(Duration.ofMinutes(7))
                        .refreshTokenTimeToLive(Duration.ofDays(5))
                        .build())
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(clientRegistration());
    }

    @Bean
    BearerTokenResolver bearerTokenResolver() {
        log.info("Bearer token resolver");
        return request -> {
            Cookie[] cookies = request.getCookies();
            if (cookies == null) return null;

            for (Cookie c : cookies) {
                if ("access_token".equals(c.getName())) {
                    log.info("Access token: {}", c.getValue());
                    return c.getValue();
                }
            }
            return null;
        };
    }

    private ClientRegistration clientRegistration() {
        return ClientRegistration.withRegistrationId("myapp")
                .clientId("app-movie")
                .clientSecret("super-secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:4200/callback")
                .scope(OidcScopes.PROFILE)
                .scope(OidcScopes.OPENID)
                .authorizationUri("http://localhost:8080/oauth2/authorize")
                .tokenUri("http://localhost:8080/oauth2/token")
                .issuerUri("http://localhost:8080")
                .jwkSetUri("http://localhost:8080/oauth2/jwks")
                .build();
    }

    @Bean
    public OAuth2AuthorizedClientRepository authorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
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
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/h2-console/**");
    }

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
}
