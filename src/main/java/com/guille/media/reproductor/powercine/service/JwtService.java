package com.guille.media.reproductor.powercine.service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import javax.crypto.SecretKey;

import com.guille.media.reproductor.powercine.utils.enums.Roles;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value(value = "${jwt.signing.key}")
    private String signinKey;

    private SecretKey key = null;

    @PostConstruct
    public void postConstruct() {
        this.key = Keys.hmacShaKeyFor(this.signinKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken() {
        Map<String, Object> claims = Map.of("authority", "ROLE_" + Roles.GUEST_USER.name(), "username",
                Roles.GUEST_USER.name());
        return this.generateToken(Roles.GUEST_USER.name(), claims);
    }

    public String generateToken(String subject, Map<String, Object> claims) {
        Date date = new Date();
        Date dateExpiration = new Date(date.getTime() + 1800000);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(date)
                .setExpiration(dateExpiration)
                .signWith(this.key, SignatureAlgorithm.HS256)
                .setClaims(claims)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getSubject(String token) {
        return this.getClaims(token)
                .getSubject();
    }

    public Boolean isExpired(String token) {
        return this.getClaims(token)
                .getExpiration().after(Date.from(Instant.now()));
    }
}
