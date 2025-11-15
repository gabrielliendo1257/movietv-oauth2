package com.guille.media.reproductor.powercine.restcontroller;

import com.guille.media.reproductor.powercine.dto.request.AuthCode;
import com.guille.media.reproductor.powercine.models.JwtAccessToken;
import com.guille.media.reproductor.powercine.service.interfaces.OAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@CrossOrigin(value = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping(value = "${api.path.base}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController {

    private final OAuthService oAuthService;

    public AuthController(OAuthService oAuthService) {
        this.oAuthService = oAuthService;
    }

    @PostMapping(value = "/auth/exchange")
    public ResponseEntity<?> tokenExchange(@RequestBody AuthCode authCode) {

        JwtAccessToken jwtAccessToken = this.oAuthService.getAccessToken(authCode.code());

        ResponseCookie cookie = ResponseCookie.from("access_token", jwtAccessToken.getAccessToken())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(jwtAccessToken.getExpiresIn())
                .build();
        log.info("Cookie: {}", cookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("ok", true));
    }

    @GetMapping(value = "/auth/me")
    public ResponseEntity<?> getMe(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ok", false));
        }

        return ResponseEntity.ok(Map.of(
                "ok", true,
                "user", authentication.getPrincipal()
        ));
    }

    @GetMapping(value = "/auth/admin")
    public ResponseEntity<?> isAdmin(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ok", false));
        }

        return ResponseEntity.ok(Map.of("ok", true));
    }
}
