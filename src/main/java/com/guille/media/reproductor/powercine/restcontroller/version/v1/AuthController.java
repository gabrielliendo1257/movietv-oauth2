package com.guille.media.reproductor.powercine.restcontroller.version.v1;

import java.time.Duration;
import java.util.Map;

import jakarta.servlet.http.HttpServletResponse;

import com.guille.media.reproductor.powercine.dto.request.AuthCode;
import com.guille.media.reproductor.powercine.dto.response.JwtAccessToken;
import com.guille.media.reproductor.powercine.service.interfaces.OAuthService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(value = "${powercine.env.frontendapp.endpoint}", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping(value = "${api.path.base}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Profile(value = {"test"})
public class AuthController
{

    @Value("${powercine.env.current.host}")
    private String currentHost;

    private final OAuthService oAuthService;

    public AuthController(OAuthService oAuthService)
    {
        this.oAuthService = oAuthService;
    }

    @PostMapping(value = "/auth/exchange")
    public ResponseEntity<?> tokenExchange(HttpServletResponse response, @RequestBody AuthCode authCode)
    {

        JwtAccessToken jwtAccessToken = this.oAuthService.getAccessToken(authCode.code());

        ResponseCookie cookie = ResponseCookie.from("access_token", jwtAccessToken.getAccessToken()).httpOnly(true).secure(false).sameSite("Strict").path("/").domain(this.currentHost).maxAge(Duration.ofMinutes(7)).build();
        log.info("Cookie: {}", cookie);

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(Map.of("ok", true));
    }

    @GetMapping(value = "/auth/me")
    public ResponseEntity<?> getMe(Authentication authentication)
    {
        if (authentication == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ok", false));
        }

        return ResponseEntity.ok(Map.of(
                "ok", true, "user", authentication.getPrincipal()
        ));
    }

    @GetMapping(value = "/auth/admin")
    public ResponseEntity<?> isAdmin(Authentication authentication)
    {
        log.info("Authentication: {}", authentication);
        if (authentication == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ok", false));
        }

        if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("ok", false));
        }

        return ResponseEntity.ok(Map.of("ok", true));
    }
}
