package com.guille.media.reproductor.powercine.auth.infrastructure.jwt;

import com.guille.media.reproductor.powercine.auth.domain.models.TokenPair;
import com.guille.media.reproductor.powercine.auth.domain.models.UserIdentity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService
{
    private final SecretKey accessTokenKey;

    private final SecretKey refreshTokenKey;

    private final Duration accessTokenExpiration;

    private final Duration refreshTokenExpiration;

    private final String issuer;

    public JwtService(
            @Value("${security.jwt.access-token-secret}")
            String accessTokenSecret,

            @Value("${security.jwt.refresh-token-secret}")
            String refreshTokenSecret,

            @Value("${security.jwt.access-token-expiration}")
            Duration accessTokenExpiration,

            @Value("${security.jwt.refresh-token-expiration}")
            Duration refreshTokenExpiration,

            @Value("${security.jwt.issuer}")
            String issuer
    ) {

        this.accessTokenKey =
                Keys.hmacShaKeyFor(
                        accessTokenSecret.getBytes()
                );

        this.refreshTokenKey =
                Keys.hmacShaKeyFor(
                        refreshTokenSecret.getBytes()
                );

        this.accessTokenExpiration =
                accessTokenExpiration;

        this.refreshTokenExpiration =
                refreshTokenExpiration;

        this.issuer = issuer;
    }

    public TokenPair generate(UserIdentity identity)
    {
        Instant now = Instant.now();

        Instant accessExpiration =
                now.plus(accessTokenExpiration);

        Instant refreshExpiration =
                now.plus(refreshTokenExpiration);

        String accessToken =
                Jwts.builder()
                        .setSubject(identity.userId().toString())
                        .setIssuer(issuer)
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(accessExpiration))
                        .setId(UUID.randomUUID().toString())
                        .claim("username", identity.username())
                        .claim("roles", identity.roles())
                        .signWith(accessTokenKey)
                        .compact();

        String refreshToken =
                Jwts.builder()
                        .setSubject(identity.userId().toString())
                        .setIssuer(issuer)
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(refreshExpiration))
                        .setId(UUID.randomUUID().toString())
                        .claim("type", "refresh")
                        .signWith(refreshTokenKey)
                        .compact();

        return new TokenPair(
                accessToken,
                refreshToken,
                accessExpiration,
                refreshExpiration
        );
    }

    public Claims parseAccessToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(this.accessTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims parseRefreshToken(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(this.refreshTokenKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isExpired(Claims claims)
    {
        return claims.getExpiration()
                .before(new Date());
    }

    public UserIdentity toIdentity(Claims claims)
    {
        Integer userId =
                Integer.parseInt(
                        claims.getSubject()
                );

        String username =
                claims.get("username", String.class);

        @SuppressWarnings("unchecked")
        List<String> roles =
                claims.get("roles", List.class);

        return new UserIdentity(
                userId,
                username,
                roles
        );
    }

    public String extractUserId(String token)
    {
        Claims claims =
                parseAccessToken(token);

        return claims.getSubject();
    }

    public boolean isRefreshToken(String token)
    {
        Claims claims =
                parseRefreshToken(token);

        String type =
                claims.get("type", String.class);

        return "refresh".equals(type);
    }
}
